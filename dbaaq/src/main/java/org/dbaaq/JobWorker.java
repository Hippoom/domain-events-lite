package org.dbaaq;

import lombok.extern.slf4j.Slf4j;
import org.dbaaq.domain.Job;
import org.dbaaq.domain.JobHandler;
import org.dbaaq.domain.JobStore;

import java.util.Optional;

import static java.lang.String.format;


@Slf4j
public class JobWorker implements Runnable {

    private JobStore jobStore;
    private JobHandler jobHandler;

    public JobWorker(JobStore jobStore, JobHandler jobHandler) {
        this.jobStore = jobStore;
        this.jobHandler = jobHandler;
    }

    public void process() {
        Optional<Job> jobOptional = jobStore.nextPending();
        jobOptional.ifPresent(pending -> {
            Optional<Job> inProgressOptional = jobStore.markInProgress(pending);
            inProgressOptional.ifPresent(inProgress -> {
                try {
                    jobHandler.handle(inProgress);
                } catch (Exception e) {
                    log.error(format("Unexpected error when handing [%s] due to %s",
                            inProgress, e.getMessage()), e);
                    jobStore.markDead(inProgress, e);
                    return;
                }
                jobStore.markDone(inProgress);
            });
        });
    }

    @Override
    public void run() {
        try {
            process();
        } catch (Exception e) {
            log.error(format("Unexpected error occurred due to %s", e.getMessage()), e);
        }
    }
}
