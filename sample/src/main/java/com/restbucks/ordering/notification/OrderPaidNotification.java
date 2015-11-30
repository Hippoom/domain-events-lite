package com.restbucks.ordering.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

@ToString
@NoArgsConstructor(access = PRIVATE)//for frameworks
@Getter
public class OrderPaidNotification {
    private String orderId;
    private double amount;

    public OrderPaidNotification(String orderId, double amount) {
        this.orderId = orderId;
        this.amount = amount;
    }


}
