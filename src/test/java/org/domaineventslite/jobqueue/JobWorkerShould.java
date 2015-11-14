package org.domaineventslite.jobqueue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class JobWorkerShould {

    @InjectMocks
    private JobWorker subject = new JobWorker();

    @Mock
    private JobStore jobStore;

    @Mock
    private JobHandler jobHandler;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void publishJob() {
        Job job = new JobFixture().build();

        when(jobStore.next()).thenReturn(of(job));

        subject.process();

        verify(jobHandler).handle(job);

        verify(jobStore).remove(job);
    }

    @Test
    public void skipSilently_givenTheJobHasBeenFetchedByOtherWorker() {

        when(jobStore.next()).thenReturn(empty());

        subject.process();
    }

}
