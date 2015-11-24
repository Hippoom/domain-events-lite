package org.dbaaq.domain;

import java.io.IOException;

import static java.text.MessageFormat.format;

public class SerializationException extends RuntimeException {

    private SerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static SerializationException whenDeserializeGiven(Class<?> aClazz,
                                                              String serializedRepresentation, IOException e) {
        return new SerializationException(format("Failed to deserialize [%s][%s] due to %s",
                aClazz, serializedRepresentation, e.getMessage()), e);
    }
}
