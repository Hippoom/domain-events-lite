package org.dbaaq;

import org.dbaaq.domain.*;

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
                jobHandler.handle(serializer.deserialize(new SerializedObject(inProgress.getContextType(), inProgress.getContext())));
                jobStore.markDone(inProgress);
            });
        });
    }
}
