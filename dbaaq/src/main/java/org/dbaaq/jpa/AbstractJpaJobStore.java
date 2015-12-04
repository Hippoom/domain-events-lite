package org.dbaaq.jpa;

import org.dbaaq.domain.Job;
import org.dbaaq.domain.JobStore;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.dbaaq.domain.Job.Status.*;
import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;


public abstract class AbstractJpaJobStore<T extends AbstractJobData> implements JobStore {

    @Transactional
    @Override
    public void store(Job job) {
        getEntityManager().persist(map(job));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Job> nextPending() {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteria = qb.createQuery(getEntityClass());
        Root<T> job = criteria.from(getEntityClass());
        criteria.where(qb.equal(job.get("status"), Job.Status.PENDING));
        criteria.orderBy(qb.asc(job.get("scheduledAt")));

        TypedQuery<T> query = getEntityManager().createQuery(criteria);
        query.setFirstResult(0);
        query.setMaxResults(1);

        List<T> jobs = query.getResultList();
        return jobs.isEmpty() ? empty() : of(map(jobs.get(0)));
    }

    @Transactional
    @Override
    public Optional<Job> markInProgress(Job pending) {
        int rowUpdated = update(pending, IN_PROGRESS);
        return rowUpdated == 1 ? of(map(findBy(pending.getId()))) : empty();
    }

    @Transactional
    @Override
    public void markDone(Job inProgress) {
        int rowUpdated = update(inProgress, DONE);
        if (rowUpdated != 1) {
            throw new OptimisticLockException(inProgress);
        }
    }

    @Transactional
    @Override
    public void markDead(Job inProgress, Exception exception) {
        int rowUpdated = update(inProgress, DEAD);
        if (rowUpdated != 1) {
            throw new OptimisticLockException(inProgress);
        }
    }

    private T findBy(String id) {
        return getEntityManager().find(getEntityClass(), id);
    }

    private int update(Job pending, Job.Status status) {
        return getEntityManager().createQuery("update " + getEntityName() + " set version = version + 1, " +
                "status = ? where id = ? and version = ?").
                setParameter(1, status).
                setParameter(2, pending.getId()).
                setParameter(3, pending.getVersion()).
                executeUpdate();
    }


    protected abstract EntityManager getEntityManager();

    protected abstract Class<T> getEntityClass();

    protected String getEntityName() {
        return getEntityClass().getSimpleName();
    }

    private T map(Job job) {
        ModelMapper modelMapper = getModelMapper();
        return modelMapper.map(job, getEntityClass());
    }

    private Job map(T jobData) {
        ModelMapper modelMapper = getModelMapper();
        return modelMapper.map(jobData, Job.class);
    }

    private ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().
                setFieldAccessLevel(PRIVATE).
                setFieldMatchingEnabled(true);
        return modelMapper;
    }


}
