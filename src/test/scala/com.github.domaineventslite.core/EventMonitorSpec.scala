package com.github.domaineventslite.core

import org.joda.time.DateTime._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FunSpec, MustMatchers}

class EventMonitorSpec extends FunSpec with MustMatchers with MockitoSugar {

  val eventStore = mock[EventStore]
  val eventPublisher = mock[EventPublisher]
  val subject = new EventMonitor(eventStore, eventPublisher)

  describe("An event publisher") {
    describe("when new events stored in the event store") {
      it("should attempt to publish them and mark them as published") {

        val orderPlaceEvent = new DomainEvent("a unique event sequence", now(),
          new OrderPlacedEvent("trackingId", "customerName"))
        val orderShippingAddressFilledEvent = new DomainEvent("another unique event sequence", now(),
          new OrderShippingAddressFilledEvent("trackingId", "Street1"))
        val unpublishedEvents = Seq(orderPlaceEvent, orderShippingAddressFilledEvent)

        when(eventStore.unpublishedEvents()).thenReturn(unpublishedEvents)

        subject.publish()

        verify(eventPublisher).publish(orderPlaceEvent)
        verify(eventStore).markAsPublished(orderPlaceEvent)
        verify(eventPublisher).publish(orderShippingAddressFilledEvent)
        verify(eventStore).markAsPublished(orderShippingAddressFilledEvent)
      }
    }
  }

}
