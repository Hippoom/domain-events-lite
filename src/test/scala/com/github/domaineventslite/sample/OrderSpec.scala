package com.github.domaineventslite.sample

import com.github.domaineventslite.core.DomainEvents
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec, ShouldMatchers}

import scala.collection.mutable.ListBuffer
import org.mockito.Mockito._

class OrderSpec extends FunSpec with ShouldMatchers with MockitoSugar with BeforeAndAfter {


  private val domainEvents = mock[DomainEvents]
  private val subject = new StubOrder("trackingId")

  before {
    subject.inject(domainEvents)
  }

  describe("An order") {
    describe("when the shipping address is filled") {
      it("should update the address and raise a shipping address filled event") {

        val eventsRaised = ListBuffer[Any]()

        DomainEvents.instance().subscribe(domainEvent => eventsRaised.append(domainEvent))

        subject.fillShippingAddress("Street1")

        subject.shippingAddress() should equal("Street1")

        verify(domainEvents).raise(new OrderShippingAddressFilledEvent("trackingId", "Street1"))

        verifyNoMoreInteractions(domainEvents)
      }
    }
  }
}

case class OrderPlacedEvent(trackingId: String, customerName: String)

case class OrderShippingAddressFilledEvent(trackingId: String, address: String)

class Order(trackingId: String) {

  private var _shippingAddress: String = ""

  def fillShippingAddress(address: String) {
    this._shippingAddress = address
    domainEvents().raise(new OrderShippingAddressFilledEvent(this.trackingId, this._shippingAddress))
  }

  def shippingAddress() = {
    _shippingAddress
  }

  def domainEvents(): DomainEvents = {
    DomainEvents.instance()
  }
}

class StubOrder(trackingId: String) extends Order(trackingId) {
  private var _domainEvents: DomainEvents = null

  def inject(mock: DomainEvents) = {
    _domainEvents = mock
  }

  override def domainEvents(): DomainEvents = {
    _domainEvents
  }
}
