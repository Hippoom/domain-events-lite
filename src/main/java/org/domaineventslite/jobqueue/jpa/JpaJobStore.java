package org.domaineventslite.jobqueue.jpa;

import org.domaineventslite.jobqueue.Job;
import org.domaineventslite.jobqueue.JobStore;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Transactional
@Component
public class JpaJobStore implements JobStore {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public Optional<Job> nextPending() {
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Job> criteria = qb.createQuery(Job.class);
        Root<Job> job = criteria.from(Job.class);
        criteria.where(qb.equal(job.get("status"), Job.Status.PENDING));
        criteria.orderBy(qb.asc(job.get("scheduledAt")));

        TypedQuery<Job> query = entityManager.createQuery(criteria);
        query.setFirstResult(0);
        query.setMaxResults(1);

        List<Job> jobs = query.getResultList();
        return jobs.isEmpty() ? empty() : of(jobs.get(0));
    }

    @Override
    public Optional<Job> markInProgress(Job pending) {
        int rowUpdated = entityManager
                .createNativeQuery("update t_job set version = version + 1, " +
                        "status = ? where id = ? and version = ?")
                .setParameter(1, Job.Status.IN_PROGRESS.name())
                .setParameter(2, pending.getId())
                .setParameter(3, pending.getVersion())
                .executeUpdate();
        return rowUpdated == 1 ? of(pending) : empty();
    }

    @Override
    public void remove(Job job) {
        Job inProgress = entityManager.find(Job.class, job.getId());
        inProgress.markDone();
    }
}
