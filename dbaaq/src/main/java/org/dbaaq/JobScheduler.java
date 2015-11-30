package org.dbaaq;

import org.dbaaq.domain.Job;
import org.dbaaq.domain.JobStore;
import org.dbaaq.domain.Serializer;

public class JobScheduler {
    private JobStore jobStore;
    private Serializer serializer;

    public JobScheduler(JobStore jobStore, Serializer serializer) {
        this.jobStore = jobStore;
        this.serializer = serializer;
    }

    public void schedule(Object payload) {
        jobStore.store(new Job(serializer.serialize(payload)));
    }
}
