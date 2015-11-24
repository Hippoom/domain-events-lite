package org.dbaaq.jackson2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dbaaq.domain.SerializationException;
import org.dbaaq.domain.FooContext;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class Jackson2SerializerShould {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ObjectMapper objectMapper = new ObjectMapper();

    private Jackson2Serializer subject = new Jackson2Serializer(objectMapper);


    @Test
    public void deserialize() {
        FooContext actual = subject.deserialize(FooContext.class, "{\"bar\":\"bar\"}");

        assertThat(actual.getBar(), is("bar"));
    }

    @Test
    public void throwsSerializationException_whenDeserialize_givenUnrecognizedRepresentation() {
        thrown.expect(SerializationException.class);

        subject.deserialize(FooContext.class, "{");
    }

}