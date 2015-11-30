package org.dbaaq.jpa;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Component
public class DefaultJpaJobStore extends AbstractJpaJobStore<DefaultJobData> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<DefaultJobData> getEntityClass() {
        return DefaultJobData.class;
    }

}
