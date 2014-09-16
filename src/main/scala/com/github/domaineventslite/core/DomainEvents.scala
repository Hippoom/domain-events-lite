package com.github.domaineventslite.core

import scala.collection.mutable.ListBuffer

object DomainEvents {
  private val subscribers = ListBuffer[Function1[Any, Unit]]()

  def subscribe(subscriber: Function1[Any, Unit]): Unit = {
    subscribers.append(subscriber)
  }

  def raise(events: Any*): Unit = {
    subscribers.toList.foreach(subsriber => events.toIterator.foreach(event => subsriber.apply(event)))

  }

}
