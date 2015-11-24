package org.dbaaq.domain;

import java.util.Optional;

public interface JobStore {

    Optional<Job> nextPending();

    Optional<Job> markInProgress(Job pending);

    void markDone(Job job);
}
