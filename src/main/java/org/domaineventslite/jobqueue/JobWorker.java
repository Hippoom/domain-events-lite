package org.domaineventslite.jobqueue;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class JobWorker {

    private JobStore jobStore;
    private JobHandler jobHandler;

    public void process() {
        Optional<Job> jobOptional = jobStore.nextPending();
        jobOptional.ifPresent(pending -> {
            Optional<Job> inProgressOptional = jobStore.markInProgress(pending);
            inProgressOptional.ifPresent(inProgress -> {
                jobHandler.handle(inProgress);
                jobStore.remove(inProgress);
            });
        });
    }
}
