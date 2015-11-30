package com.restbucks.ordering.notification;

import com.restbucks.ordering.container.RestbucksContainer;
import org.dbaaq.JobScheduler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.is;

/**
 * Created by Yugang.Zhou on 11/29/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestbucksContainer.class)
public class OrderPaidNotificationSendingIntegrationTest {

    @Resource
    private JobScheduler jobScheduler;

    @Resource
    private OrderPaidNotificationHandler sender;

    @Test
    public void sendNotificationOneByOne() throws Exception {
        OrderPaidNotification notification1 = new OrderPaidNotification("1", 10.00);
        OrderPaidNotification notification2 = new OrderPaidNotification("2", 12.00);

        jobScheduler.schedule(notification1);
        jobScheduler.schedule(notification2);

        await().atMost(2, SECONDS).until(() -> sender.getCount(), is(2));
    }
}