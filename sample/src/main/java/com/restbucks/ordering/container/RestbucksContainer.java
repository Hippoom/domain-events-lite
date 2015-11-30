package com.restbucks.ordering.container;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restbucks.ordering.notification.OrderPaidNotificationHandler;
import com.restbucks.ordering.notification.OrderReadyNotificationHandler;
import org.dbaaq.JobScheduler;
import org.dbaaq.JobWorker;
import org.dbaaq.SimpleJobScheduler;
import org.dbaaq.domain.JobStore;
import org.dbaaq.domain.Serializer;
import org.dbaaq.jackson2.Jackson2Serializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Import(PersistenceConfig.class)
@ComponentScan("com.restbucks.ordering")
@Configuration
public class RestbucksContainer {
    @Resource(name = "orderPaidNotificationJobExecutor")
    private ScheduledThreadPoolExecutor orderPaidNotificationJobExecutor;

    @Resource(name = "orderPaidNotificationJobWorker")
    private JobWorker orderPaidNotificationJobWorker;

    @Resource(name = "orderReadyNotificationJobExecutor")
    private ScheduledThreadPoolExecutor orderReadyNotificationJobExecutor;


    @Resource(name = "orderReadyNotificationJobWorker")
    private JobWorker orderReadyNotificationJobWorker;

    @Bean
    protected ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    protected Serializer jacksonSerializer(ObjectMapper objectMapper) {
        return new Jackson2Serializer(objectMapper);
    }

    @Bean
    protected JobScheduler orderPaidNotificationJobScheduler(@Qualifier("orderPaidNotificationJobStore") JobStore orderPaidNotificationJobStore,
                                                             Serializer serializer) {
        return new SimpleJobScheduler(orderPaidNotificationJobStore, serializer);
    }

    @Bean
    protected JobScheduler orderReadyNotificationJobScheduler(@Qualifier("orderReadyNotificationJobStore") JobStore orderReadyNotificationJobStore,
                                                              Serializer serializer) {
        return new SimpleJobScheduler(orderReadyNotificationJobStore, serializer);
    }

    @Bean
    protected JobWorker orderPaidNotificationJobWorker(@Qualifier("orderPaidNotificationJobStore") JobStore orderPaidNotificationJobStore,
                                                       OrderPaidNotificationHandler jobHandler) {
        return new JobWorker(orderPaidNotificationJobStore, jobHandler);
    }

    @Bean
    protected JobWorker orderReadyNotificationJobWorker(@Qualifier("orderReadyNotificationJobStore") JobStore orderReadyNotificationJobStore,
                                                        OrderReadyNotificationHandler jobHandler) {
        return new JobWorker(orderReadyNotificationJobStore, jobHandler);
    }

    @Bean
    protected ScheduledThreadPoolExecutor orderPaidNotificationJobExecutor() {
        return new ScheduledThreadPoolExecutor(1);
    }

    @Bean
    protected ScheduledThreadPoolExecutor orderReadyNotificationJobExecutor() {
        return new ScheduledThreadPoolExecutor(1);
    }

    @PostConstruct
    protected void configureJobWorker() {
        orderPaidNotificationJobExecutor.scheduleAtFixedRate(orderPaidNotificationJobWorker, 100, 100, MILLISECONDS);
        orderReadyNotificationJobExecutor.scheduleAtFixedRate(orderReadyNotificationJobWorker, 100, 100, MILLISECONDS);
    }
}
