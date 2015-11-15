package org.domaineventslite.jobqueue;

import java.util.Optional;

public interface JobStore {

    Optional<Job> nextPending();

    Optional<Job> markInProgress(Job pending);

    void remove(Job job);
}
