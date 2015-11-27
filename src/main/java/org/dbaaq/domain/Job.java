package org.dbaaq.domain;

import lombok.Getter;

import java.util.Date;

import static org.dbaaq.domain.Job.Status.PENDING;

@Getter
public class Job {
    private String id;

    private int version;

    private Status status = PENDING;

    private Date scheduledAt;

    private byte[] payload;

    public enum Status {
        PENDING, IN_PROGRESS, DONE
    }
}