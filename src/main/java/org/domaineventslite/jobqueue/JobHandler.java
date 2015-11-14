package org.domaineventslite.jobqueue;

public interface JobHandler {


    void handle(Job job);
}
