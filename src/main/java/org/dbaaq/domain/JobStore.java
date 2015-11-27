package org.dbaaq.domain;

import java.util.Optional;

public interface JobStore {

    void store(Job job);

    Optional<Job> nextPending();

    Optional<Job> markInProgress(Job pending);

    void markDone(Job job);
}
