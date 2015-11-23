package org.domaineventslite.jobqueue;

import lombok.Getter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Yugang.Zhou on 11/23/15.
 */
public class SimpleJobHandlerShould {

    @InjectMocks
    private SimpleJobHandlerStub subject = new SimpleJobHandlerStub();

    @Mock
    private Serializer serializer;

    @Before
    public void initTestDoubles() {
        initMocks(this);
    }

    @Test
    public void deserializeJobContext() {

        FooContext context = new FooContext("abc");
        Job job = new JobFixture().context(context).build();

        when(serializer.deserialize(FooContext.class, context.toString())).
                thenReturn(context);

        subject.handle(job);

        assertThat(subject.getReceived(), is(context));
    }

    private class SimpleJobHandlerStub extends SimpleJobHandler<FooContext> {
        @Getter
        private FooContext received;

        @Override
        protected void handle(FooContext context) {
            this.received = context;
        }
    }
}