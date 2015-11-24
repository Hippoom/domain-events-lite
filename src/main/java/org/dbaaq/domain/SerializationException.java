package org.dbaaq.domain;

import static java.text.MessageFormat.format;

public class SerializationException extends RuntimeException {

    private SerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    private static SerializationException whenDeserializeGiven(String aClazz,
                                                               String serializedRepresentation, Throwable e) {
        return new SerializationException(format("Failed to deserialize [%s][%s] due to %s",
                aClazz, serializedRepresentation, e.getMessage()), e);
    }

    public static SerializationException whenDeserializeGiven(SerializedObject serializedObject, Throwable e) {
        return whenDeserializeGiven(serializedObject.getType(), serializedObject.getData(), e);
    }
}
