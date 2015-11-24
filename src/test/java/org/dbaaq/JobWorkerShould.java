package org.dbaaq;

import org.dbaaq.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class JobWorkerShould {

    @InjectMocks
    private JobWorker subject = new JobWorker();

    @Mock
    private JobStore jobStore;

    @Mock
    private Serializer serializer;

    @Mock
    private JobHandler jobHandler;
    private final FooContext context = new FooContext();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void dispatchDeserializedJobContext() {
        Job pending = new JobFixture().build();

        when(jobStore.nextPending()).thenReturn(of(pending));

        Job inProgress = new JobFixture().inProgress().build();

        when(jobStore.markInProgress(pending)).thenReturn(of(inProgress));

        when(serializer.deserialize(refEq(new SerializedObject(FooContext.class.getTypeName(), pending.getContext())))).
                thenReturn(context);

        subject.process();

        verify(jobHandler).handle(context);

        verify(jobStore).markDone(inProgress);
    }

    @Test
    public void skipSilently_givenNoPendingJob() {

        when(jobStore.nextPending()).thenReturn(empty());

        subject.process();
    }

    @Test
    public void skipSilently_givenTheJobHasBeenFetchedByOtherWorker() {
        Job pending = new JobFixture().build();

        when(jobStore.nextPending()).thenReturn(of(pending));

        when(jobStore.markInProgress(pending)).thenReturn(empty());

        subject.process();

        verifyZeroInteractions(jobHandler);
    }

}
