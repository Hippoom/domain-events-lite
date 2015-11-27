package org.dbaaq;

import org.dbaaq.domain.Job;
import org.dbaaq.domain.JobHandler;
import org.dbaaq.domain.JobStore;

import java.util.Optional;


public class JobWorker {

    private JobStore jobStore;
    private JobHandler jobHandler;

    public void process() {
        Optional<Job> jobOptional = jobStore.nextPending();
        jobOptional.ifPresent(pending -> {
            Optional<Job> inProgressOptional = jobStore.markInProgress(pending);
            inProgressOptional.ifPresent(inProgress -> {
                jobHandler.handle(inProgress);
                jobStore.markDone(inProgress);
            });
        });
    }
}
