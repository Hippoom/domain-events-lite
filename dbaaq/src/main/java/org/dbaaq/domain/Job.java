package org.dbaaq.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

import static org.dbaaq.domain.Job.Status.PENDING;

@ToString(exclude = "payload")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE) // for frameworks only
public class Job {
    private String id;

    private int version;

    private Status status = PENDING;

    private Date scheduledAt;

    private byte[] payload;

    public Job(byte[] payload) {
        this.id = UUID.randomUUID().toString();
        this.scheduledAt = new Date();
        this.payload = payload;
    }

    public enum Status {
        PENDING, IN_PROGRESS, DONE
    }
}