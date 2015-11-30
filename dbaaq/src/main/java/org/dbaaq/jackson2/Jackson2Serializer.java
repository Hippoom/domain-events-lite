package org.dbaaq.jackson2;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dbaaq.domain.Serializer;

import java.io.IOException;

import static org.dbaaq.domain.SerializationException.whenDeserializeGiven;
import static org.dbaaq.domain.SerializationException.whenSerializeGiven;

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

    @Override
    public byte[] serialize(Object payload) {
        try {
            return objectMapper.writeValueAsBytes(payload);
        } catch (JsonProcessingException e) {
            throw whenSerializeGiven(payload, e);
        }
    }
}
