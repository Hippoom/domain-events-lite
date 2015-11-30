package com.restbucks.ordering.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

@ToString
@NoArgsConstructor(access = PRIVATE)//for frameworks
@Getter
public class OrderReadyNotification {
    private String orderId;

    public OrderReadyNotification(String orderId) {
        this.orderId = orderId;
    }
}
