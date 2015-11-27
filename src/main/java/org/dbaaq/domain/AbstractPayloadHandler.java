package org.dbaaq.domain;

public abstract class AbstractPayloadHandler<T> implements JobHandler {

    private Serializer serializer;

    public AbstractPayloadHandler(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public void handle(Job job) {
        doHandle(serializer.deserialize(getPayloadType(), job.getPayload()));
    }

    protected abstract void doHandle(T payload);

    protected abstract Class<T> getPayloadType();
}
