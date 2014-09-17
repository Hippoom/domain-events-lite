package com.github.domaineventslite.core

import org.scalatest.{FunSpec, MustMatchers}

import scala.collection.mutable.ListBuffer

class DomainEventsSpec extends FunSpec with MustMatchers {

  private val subject = new DomainEvents()

  describe("A DomainEvents instance") {
    describe("when receiving domain events") {
      it("should append the events in the event store") {

        val eventsRaised = ListBuffer[Any]()

        subject.subscribe(event => eventsRaised.append(event))

        val orderPlacedEvent = new OrderPlacedEvent("trackingId", "customerName")
        val orderShippingAddressFilledEvent = new OrderShippingAddressFilledEvent("trackingId", "street1")

        subject.raise(orderPlacedEvent, orderShippingAddressFilledEvent)

        eventsRaised.toSeq must equal(Seq(orderPlacedEvent, orderShippingAddressFilledEvent))
      }
    }
  }
}

case class OrderPlacedEvent(trackingId: String, customerName: String)

case class OrderShippingAddressFilledEvent(trackingId: String, address: String)



