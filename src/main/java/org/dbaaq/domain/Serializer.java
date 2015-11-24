package org.dbaaq.domain;

public interface Serializer {
    <T> T deserialize(SerializedObject serializedObject);
}
