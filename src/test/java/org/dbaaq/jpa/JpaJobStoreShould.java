package org.dbaaq.jpa;

import com.github.hippoom.springtestdbunit.dataset.GivenWhenThenFlatXmlDataSetLoader;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.dbaaq.domain.Job;
import org.dbaaq.domain.JobFixture;
import org.dbaaq.domain.JobStore;
import org.domaineventslite.jobqueue.jpa.DatabaseConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DatabaseConfig.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DbUnitConfiguration(dataSetLoader = GivenWhenThenFlatXmlDataSetLoader.class)
@DatabaseSetup(value = "classpath:all.xml", type = DatabaseOperation.DELETE_ALL)
public class JpaJobStoreShould {

    @Resource(name = "jpaJobStore")
    private JobStore subject;


    @Test
    public void fetchFirstJob() {
        LocalDateTime now = LocalDateTime.now();

        subject.store(new JobFixture("1").done().scheduledAt(now).build());
        subject.store(new JobFixture("2").inProgress().scheduledAt(now).build());
        subject.store(new JobFixture("3").scheduledAt(now.plusSeconds(1)).build());
        subject.store(new JobFixture("4").scheduledAt(now.plusSeconds(2)).build());

        Optional<Job> jobOptional = subject.nextPending();

        assertThat(jobOptional.get().getId(), is("3"));
    }

    @Test
    public void returnsEmpty_whenFetchNext_givenNoPendingJobs() {
        Optional<Job> jobOptional = subject.nextPending();

        assertThat(jobOptional.isPresent(), is(false));
    }

    @DatabaseSetup("given:classpath:job_mark_in_progress.xml")
    @ExpectedDatabase(value = "then:classpath:job_mark_in_progress.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    @Test
    public void updateStatus_whenMarkInProgress() {
        Optional<Job> pending = subject.nextPending();

        Optional<Job> jobOptional = subject.markInProgress(pending.get());

        assertThat(jobOptional.isPresent(), is(true));
    }

    @DatabaseSetup("given:classpath:job_mark_in_progress.xml")
    @ExpectedDatabase(value = "then:classpath:job_mark_in_progress.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    @Test
    public void updateStatus_whenMarkInProgressConcurrently() throws InterruptedException {
        Optional<Job> pending = subject.nextPending();

        Optional<Job> one = subject.markInProgress(pending.get());
        Optional<Job> another = subject.markInProgress(pending.get());

        assertThat(one.isPresent() || another.isPresent(), is(true));
        assertThat(one.isPresent() && another.isPresent(), is(false));
    }

    @DatabaseSetup("given:classpath:job_remove.xml")
    @ExpectedDatabase(value = "then:classpath:job_remove.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    @Test
    public void updateStatus_whenRemove() throws InterruptedException {
        Optional<Job> pending = subject.nextPending();

        Optional<Job> jobOptional = subject.markInProgress(pending.get());

        subject.markDone(jobOptional.get());
    }
}
