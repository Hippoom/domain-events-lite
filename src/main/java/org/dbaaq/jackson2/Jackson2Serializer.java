package org.dbaaq.jackson2;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.dbaaq.domain.SerializationException;
import org.dbaaq.domain.SerializedObject;
import org.dbaaq.domain.Serializer;

import java.io.IOException;

public class Jackson2Serializer implements Serializer {

    private ObjectMapper objectMapper;

    public Jackson2Serializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> T deserialize(SerializedObject serializedObject) {
        try {
            return (T) objectMapper.readValue(serializedObject.getData(), classFor(serializedObject.getType()));
        } catch (IOException | ClassNotFoundException e) {
            throw SerializationException.whenDeserializeGiven(serializedObject, e);

        }
    }

    private Class<?> classFor(String type) throws ClassNotFoundException {
        return TypeFactory.defaultInstance().findClass(type);
    }
}
