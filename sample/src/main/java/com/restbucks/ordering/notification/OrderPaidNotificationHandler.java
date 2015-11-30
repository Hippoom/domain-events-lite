package com.restbucks.ordering.notification;

import org.dbaaq.domain.AbstractPayloadHandler;
import org.dbaaq.domain.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderPaidNotificationHandler extends AbstractPayloadHandler<OrderPaidNotification> {

    private int count;

    @Autowired
    public OrderPaidNotificationHandler(Serializer serializer) {
        super(serializer);
    }

    @Override
    protected void doHandle(OrderPaidNotification payload) {
        count++;
    }

    @Override
    protected Class getPayloadType() {
        return OrderPaidNotification.class;
    }

    public int getCount() {
        return count;
    }
}
