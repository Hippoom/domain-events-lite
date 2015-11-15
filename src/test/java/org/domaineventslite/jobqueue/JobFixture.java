package org.domaineventslite.jobqueue;

public class JobFixture {

    private final Job job = new Job("1");

    public JobFixture inProgress() {
        job.markInProgress();
        return this;
    }

    public Job build() {
        return job;
    }
}
