package com.github.domaineventslite.core

import scala.collection.mutable.ListBuffer

class DomainEvents {
  private val subscribers = ListBuffer[(Any) => Unit]()

  def subscribe(subscriber: (Any) => Unit): Unit = {
    subscribers.append(subscriber)
  }

  def raise(events: Any*): Unit = {
    subscribers.toList.foreach(subscriber => {
      println(subscriber)
      events.toIterator.foreach(event => subscriber.apply(event))
    })
  }

}

object DomainEvents {
  private val singleton = new DomainEvents()

  def instance(): DomainEvents = {
    singleton
  }
}
