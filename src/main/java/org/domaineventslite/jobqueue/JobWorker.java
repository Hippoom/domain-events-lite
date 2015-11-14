package org.domaineventslite.jobqueue;

import java.util.Optional;

public class JobWorker {

    private JobStore jobStore;
    private JobHandler jobHandler;

    public void process() {
        Optional<Job> jobOptional = jobStore.next();
        jobOptional.ifPresent(job -> {
            jobHandler.handle(job);
            jobStore.remove(job);
        });
    }
}
