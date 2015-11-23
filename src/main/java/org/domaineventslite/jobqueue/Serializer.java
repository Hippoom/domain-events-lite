package org.domaineventslite.jobqueue;

public interface Serializer {
    <T> T deserialize(Class<T> aClass, String context);
}
