package org.domaineventslite.jobqueue;

import java.util.Hashtable;
import java.util.Map;

public class JobDispatcher implements JobHandler {
    private Map<Class<?>, JobHandler> handlers = new Hashtable<>();
    private JobHandler deadLetterJobHandler;

    @Override
    public void handle(Job job) {
        JobHandler jobHandler = getJobHandlerFor(job);
        jobHandler.handle(job);
    }

    private JobHandler getJobHandlerFor(Job job) {
        Class<?> jobContextType = null;
        try {
            jobContextType = Class.forName(job.getContextType());
        } catch (ClassNotFoundException e) {
            return deadLetterJobHandler;
        }
        JobHandler handlerMaybe = getJobHandler(jobContextType);
        return handlerMaybe == null ? deadLetterJobHandler : handlerMaybe;
    }

    private JobHandler getJobHandler(Class<?> jobContextType) {
        return this.handlers.get(jobContextType);
    }

    public void register(Class<?> jobContextType, JobHandler jobHandler) {
        JobHandler handler = getJobHandler(jobContextType);
        if (handler == null) {
            this.handlers.put(jobContextType, jobHandler);
        } else {
            throw new IllegalArgumentException("Cannot register " + jobHandler +
                    " + to handle " + jobContextType +
                    " due to handler " + handler + " has been registered already.");
        }
    }
}
