package org.dbaaq.domain;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import static org.dbaaq.domain.Job.Status.IN_PROGRESS;

public class JobFixture {

    private final Job job = new Job("1");

    public JobFixture() {
        set("contextType", FooContext.class.getCanonicalName());
        set("context", "{\"bar\":\"bar\"}");
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

    private void mark(Job.Status status) {
        set("status", status);
    }


    public Job build() {
        return job;
    }

}
