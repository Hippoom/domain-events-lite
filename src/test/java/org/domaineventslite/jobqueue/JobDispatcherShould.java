package org.domaineventslite.jobqueue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class JobDispatcherShould {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private JobDispatcher subject = new JobDispatcher();

    @Mock
    private JobHandler jobHandler;

    @Mock
    private JobHandler deadLetterJobHandler;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        subject.register(FooContext.class, jobHandler);
    }

    @Test
    public void delegateJobToCorrespondingHandler() {
        Job job = new JobFixture().context(new FooContext()).build();

        subject.handle(job);

        verify(jobHandler).handle(job);
        verifyZeroInteractions(deadLetterJobHandler);
    }

    @Test
    public void dispatchJobToDeadLetterHandler() {
        Job job = new JobFixture().context(new SomeContextWeDontKnow()).build();

        subject.handle(job);

        verify(deadLetterJobHandler).handle(job);
        verifyZeroInteractions(jobHandler);
    }

    @Test
    public void throw_givenDuplicateRegistration() {
        JobHandler duplicateHandler = mock(JobHandler.class);

        thrown.expectMessage("Cannot register " + duplicateHandler +
                " + to handle " + FooContext.class +
                " due to handler " + jobHandler + " has been registered already.");


        subject.register(FooContext.class, duplicateHandler);

    }

    public static class FooContext {

    }

    public static class SomeContextWeDontKnow {

    }
}
