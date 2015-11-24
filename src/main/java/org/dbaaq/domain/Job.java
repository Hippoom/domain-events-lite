package org.dbaaq.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@Getter
public class Job {
    private String id;

    private int version = 1;

    private Status status = Status.PENDING;

    private Date scheduledAt;

    private String contextType;

    private String context;

    public Job(String id) {
        this.id = id;
    }

    public enum Status {
        PENDING, IN_PROGRESS, DONE
    }
}