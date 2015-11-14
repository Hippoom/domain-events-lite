package org.domaineventslite.jobqueue;

import java.util.Optional;

public interface JobStore {
    
    Optional<Job> next();

    void remove(Job job);
}
