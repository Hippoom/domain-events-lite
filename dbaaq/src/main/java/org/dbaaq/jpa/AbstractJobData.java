package org.dbaaq.jpa;

import lombok.Getter;
import org.dbaaq.domain.Job;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.EnumType.STRING;

@MappedSuperclass
@Getter
public class AbstractJobData {
    @Id
    private String id;
    @Version
    private int version = 1;
    @Enumerated(value = STRING)
    private Job.Status status = Job.Status.PENDING;
    @Column(name = "scheduled_at")
    private Date scheduledAt;
    @Lob
    private byte[] payload;
}
