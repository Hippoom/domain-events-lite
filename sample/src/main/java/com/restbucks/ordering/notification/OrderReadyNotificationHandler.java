package com.restbucks.ordering.notification;

import org.dbaaq.domain.AbstractPayloadHandler;
import org.dbaaq.domain.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderReadyNotificationHandler extends AbstractPayloadHandler<OrderReadyNotification> {

    private int count;

    @Autowired
    public OrderReadyNotificationHandler(Serializer serializer) {
        super(serializer);
    }

    @Override
    protected void doHandle(OrderReadyNotification payload) {
        count++;
    }

    @Override
    protected Class getPayloadType() {
        return OrderReadyNotification.class;
    }

    public int getCount() {
        return count;
    }
}
