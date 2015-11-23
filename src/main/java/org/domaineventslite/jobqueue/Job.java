package org.domaineventslite.jobqueue;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_job")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Job {
    @Id
    private String id;

    @Version
    private int version = 1;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(name = "scheduled_at")
    private Date scheduledAt;

    @Column(name = "context_type")
    private String contextType;

    @Lob
    private String context;

    public Job(String id) {
        this.id = id;
    }

    public void markInProgress() {
        this.status = Status.IN_PROGRESS;
    }

    public void markDone() {
        this.status = Status.DONE;
    }

    public String getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public Object getContext() {
        return context;
    }

    protected void attach(Object context) {
        this.context = context.toString();
        this.contextType = context.getClass().getName();
    }

    public String getContextType() {
        return contextType;
    }


    public enum Status {
        PENDING, IN_PROGRESS, DONE
    }
}