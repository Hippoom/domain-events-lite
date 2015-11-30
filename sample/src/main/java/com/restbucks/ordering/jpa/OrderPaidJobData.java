package com.restbucks.ordering.jpa;

import org.dbaaq.jpa.AbstractJobData;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_order_paid_notification")
public class OrderPaidJobData extends AbstractJobData {
}
