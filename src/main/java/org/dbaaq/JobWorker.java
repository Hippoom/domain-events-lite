package org.dbaaq;

import org.dbaaq.domain.Job;
import org.dbaaq.domain.JobHandler;
import org.dbaaq.domain.JobStore;
import org.dbaaq.domain.Serializer;

import java.util.Optional;


public class JobWorker {

    private JobStore jobStore;
    private Serializer serializer;
    private JobHandler jobHandler;

    public void process() {
        Optional<Job> jobOptional = jobStore.nextPending();
        jobOptional.ifPresent(pending -> {
            Optional<Job> inProgressOptional = jobStore.markInProgress(pending);
            inProgressOptional.ifPresent(inProgress -> {
                try {
                    jobHandler.handle(serializer.deserialize(
                            Class.forName(inProgress.getContextType(), true, getClass().getClassLoader()), inProgress.getContext()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                jobStore.markDone(inProgress);
            });
        });
    }
}
