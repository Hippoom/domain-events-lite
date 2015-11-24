package org.dbaaq.jackson2;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.dbaaq.domain.SerializationException;
import org.dbaaq.domain.Serializer;

import java.io.IOException;

public class Jackson2Serializer implements Serializer {

    private ObjectMapper objectMapper;

    public Jackson2Serializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> T deserialize(Class<T> aClazz, String serializedRepresentation) {
        try {
            return objectMapper.readValue(serializedRepresentation, aClazz);
        } catch (IOException e) {
            throw SerializationException.whenDeserializeGiven(aClazz, serializedRepresentation, e);
        }
    }
}
