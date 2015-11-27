package org.dbaaq.jackson2;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.dbaaq.domain.Serializer;

import java.io.IOException;

import static org.dbaaq.domain.SerializationException.whenDeserializeGiven;

public class Jackson2Serializer implements Serializer {

    private ObjectMapper objectMapper;

    public Jackson2Serializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] representation) {
        try {
            return objectMapper.readValue(representation, clazz);
        } catch (IOException e) {
            throw whenDeserializeGiven(clazz, representation, e);
        }
    }
}
