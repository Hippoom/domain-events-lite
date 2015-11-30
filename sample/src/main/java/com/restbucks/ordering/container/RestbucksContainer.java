package com.restbucks.ordering.container;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restbucks.ordering.notification.OrderPaidNotificationHandler;
import org.dbaaq.JobScheduler;
import org.dbaaq.JobWorker;
import org.dbaaq.domain.JobStore;
import org.dbaaq.domain.Serializer;
import org.dbaaq.jackson2.Jackson2Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Import(PersistenceConfig.class)
@ComponentScan("com.restbucks.ordering")
@Configuration
public class RestbucksContainer {
    @Autowired
    private ScheduledThreadPoolExecutor executor;

    @Autowired
    private JobWorker jobWorker;

    @Bean
    protected ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    protected Serializer jacksonSerializer(ObjectMapper objectMapper) {
        return new Jackson2Serializer(objectMapper);
    }

    @Bean
    protected JobScheduler jobScheduler(JobStore jobStore, Serializer serializer) {
        return new JobScheduler(jobStore, serializer);
    }

    @Bean
    protected JobWorker jobWorker(JobStore jobStore, OrderPaidNotificationHandler jobHandler) {
        return new JobWorker(jobStore, jobHandler);
    }

    @Bean
    protected ScheduledThreadPoolExecutor executor() {
        return new ScheduledThreadPoolExecutor(1);
    }

    @PostConstruct
    protected void configureJobWorker() {
        executor.scheduleAtFixedRate(jobWorker, 100, 100, MILLISECONDS);
    }
}
