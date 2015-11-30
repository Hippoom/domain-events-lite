package com.restbucks.ordering.jpa;

import org.dbaaq.jpa.AbstractJobData;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_order_ready_notification")
public class OrderReadyJobData extends AbstractJobData {
}
