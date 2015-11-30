package org.dbaaq.jpa;

import org.dbaaq.domain.Job;
import org.dbaaq.domain.JobStore;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.dbaaq.domain.Job.Status.DONE;
import static org.dbaaq.domain.Job.Status.IN_PROGRESS;
import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Transactional
@Component
public class JpaJobStore implements JobStore {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void store(Job job) {
        entityManager.persist(map(job));
    }

    private JobData map(Job job) {
        ModelMapper modelMapper = getModelMapper();
        return modelMapper.map(job, JobData.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Job> nextPending() {
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<JobData> criteria = qb.createQuery(JobData.class);
        Root<JobData> job = criteria.from(JobData.class);
        criteria.where(qb.equal(job.get("status"), Job.Status.PENDING));
        criteria.orderBy(qb.asc(job.get("scheduledAt")));

        TypedQuery<JobData> query = entityManager.createQuery(criteria);
        query.setFirstResult(0);
        query.setMaxResults(1);

        List<JobData> jobs = query.getResultList();
        return jobs.isEmpty() ? empty() : of(map(jobs.get(0)));
    }

    private Job map(JobData jobData) {
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

    @Override
    public Optional<Job> markInProgress(Job pending) {
        int rowUpdated = update(pending, IN_PROGRESS);
        return rowUpdated == 1 ? of(map(findBy(pending.getId()))) : empty();
    }

    private JobData findBy(String id) {
        return entityManager.find(JobData.class, id);
    }

    private int update(Job pending, Job.Status status) {
        return entityManager.createNativeQuery("update t_job set version = version + 1, " +
                "status = ? where id = ? and version = ?").
                setParameter(1, status.name()).
                setParameter(2, pending.getId()).
                setParameter(3, pending.getVersion()).
                executeUpdate();
    }

    @Override
    public void markDone(Job inProgress) {
        int rowUpdated = update(inProgress, DONE);
        if (rowUpdated != 1) {
            throw new OptimisticLockException(inProgress);
        }
    }
}
