package com.github.domaineventslite.core

import org.scalatest.{FunSpec, MustMatchers}

class DomainEventsSpec extends FunSpec with MustMatchers {

  private val eventStore = new StubEventStore()

  describe("A DomainEvents instance") {
    describe("when receiving domain events") {
      it("should append the events in the event store") {

        DomainEvents.subscribe(eventStore)

        val orderPlacedEvent = new OrderPlacedEvent("trackingId", "customerName")
        val orderShippingAddressFilledEvent = new OrderShippingAddressFilledEvent("trackingId", "street1")

        DomainEvents.raise(orderPlacedEvent, orderShippingAddressFilledEvent)

        eventStore.received must equal(Seq(orderPlacedEvent, orderShippingAddressFilledEvent))
      }
    }
  }
}

class OrderPlacedEvent(val trackingId: String, val customerName: String)

class OrderShippingAddressFilledEvent(val trackingId: String, val address: String)

import scala.collection.mutable.ListBuffer

class StubEventStore extends EventStore with Function1[Any, Unit] {

  private val eventStorage: ListBuffer[Any] = ListBuffer[Any]()

  override def append(domainEvent: Any): Unit = {
    this.eventStorage.append(domainEvent)
  }

  override def apply(domainEvent: Any): Unit = {
    append(domainEvent)
  }

  def received: Seq[Any] = {
    eventStorage
  }

}