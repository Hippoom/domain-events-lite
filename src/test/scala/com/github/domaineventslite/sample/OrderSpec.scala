package com.github.domaineventslite.sample

import com.github.domaineventslite.core.DomainEvents
import org.scalatest.{FunSpec, ShouldMatchers}

import scala.collection.mutable.ListBuffer

class OrderSpec extends FunSpec with ShouldMatchers {

  private val subject = new Order("trackingId")

  describe("An order") {
    describe("when the shipping address is filled") {
      it("should update the address and raise a shipping address filled event") {

        val eventsRaised = ListBuffer[Any]()

        DomainEvents.subscribe(domainEvent => eventsRaised.append(domainEvent))

        subject.fillShippingAddress("Street1")

        subject.shippingAddress() should equal("Street1")

        eventsRaised should equal (Seq(new OrderShippingAddressFilledEvent("trackingId", "Street1")))
      }
    }
  }
}

case class OrderPlacedEvent(val trackingId: String, val customerName: String)

case class OrderShippingAddressFilledEvent(val trackingId: String, val address: String)

class Order(val trackingId: String) {

  private var _shippingAddress: String = ""

  def fillShippingAddress(address: String) {
    this._shippingAddress = address
    DomainEvents.raise(new OrderShippingAddressFilledEvent(this.trackingId, this._shippingAddress))
  }

  def shippingAddress() = {
    _shippingAddress
  }
}
