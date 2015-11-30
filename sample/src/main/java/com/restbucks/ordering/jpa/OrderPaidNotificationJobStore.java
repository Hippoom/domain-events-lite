package com.restbucks.ordering.jpa;

import org.dbaaq.jpa.AbstractJpaJobStore;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class OrderPaidNotificationJobStore extends AbstractJpaJobStore<OrderPaidJobData> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<OrderPaidJobData> getEntityClass() {
        return OrderPaidJobData.class;
    }
}
