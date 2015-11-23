package org.domaineventslite.jobqueue;

public abstract class SimpleJobHandler<T> implements JobHandler {
    private Serializer serializer;

    @Override
    public void handle(Job job) {
        try {
            handle((T) serializer.deserialize(Class.forName(job.getContextType()), job.getContext()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected abstract void handle(T context);


}
