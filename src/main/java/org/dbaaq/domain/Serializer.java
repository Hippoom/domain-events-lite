package org.dbaaq.domain;

public interface Serializer {
    <T> T deserialize(Class<T> clazz, byte[] representation);
}
