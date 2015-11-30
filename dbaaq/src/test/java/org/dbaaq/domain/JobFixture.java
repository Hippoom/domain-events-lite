package org.dbaaq.domain;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static java.time.ZoneId.systemDefault;
import static org.dbaaq.domain.Job.Status.DONE;
import static org.dbaaq.domain.Job.Status.IN_PROGRESS;

public class JobFixture {

    private final Job job = new Job(new byte[]{});


    public JobFixture() {
        this(UUID.randomUUID().toString());
    }

    public JobFixture(String id) {
        set("id", id);
        setScheduledAt(new Date());
        set("payload", "{\"bar\":\"bar\"}".getBytes());
    }

    private void set(String fieldName, Object value) {
        Field field = ReflectionUtils.findField(Job.class, fieldName);
        field.setAccessible(true);
        ReflectionUtils.setField(field, job, value);
    }

    public JobFixture inProgress() {
        mark(IN_PROGRESS);
        return this;
    }

    public JobFixture done() {
        mark(DONE);
        return this;
    }

    public JobFixture scheduledAt(LocalDateTime date) {
        setScheduledAt(Date.from(date.atZone(systemDefault()).toInstant()));
        return this;
    }

    private void setScheduledAt(Date date) {
        set("scheduledAt", date);
    }

    private void mark(Job.Status status) {
        set("status", status);
    }


    public Job build() {
        return job;
    }

}
