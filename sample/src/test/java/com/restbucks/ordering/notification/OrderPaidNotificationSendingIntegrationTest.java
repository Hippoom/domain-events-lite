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

    @Resource(name = "orderPaidNotificationJobScheduler")
    private JobScheduler orderPaidNotificationJobScheduler;

    @Resource(name = "orderReadyNotificationJobScheduler")
    private JobScheduler orderReadyNotificationJobScheduler;

    @Resource
    private OrderReadyNotificationHandler orderReadyNotificationSender;

    @Resource
    private OrderPaidNotificationHandler orderPaidNotificationSender;

    @Test
    public void sendPaidNotificationOneByOne() throws Exception {
        OrderPaidNotification notification1 = new OrderPaidNotification("1", 10.00);
        OrderPaidNotification notification2 = new OrderPaidNotification("2", 12.00);

        orderPaidNotificationJobScheduler.schedule(notification1);
        orderPaidNotificationJobScheduler.schedule(notification2);

        await().atMost(2, SECONDS).until(() -> orderPaidNotificationSender.getCount(), is(2));
    }

    @Test
    public void sendReadyNotificationOneByOne() throws Exception {
        OrderReadyNotification notification1 = new OrderReadyNotification("1");
        OrderReadyNotification notification2 = new OrderReadyNotification("2");

        orderReadyNotificationJobScheduler.schedule(notification1);
        orderReadyNotificationJobScheduler.schedule(notification2);

        await().atMost(2, SECONDS).until(() -> orderReadyNotificationSender.getCount(), is(2));
    }
}