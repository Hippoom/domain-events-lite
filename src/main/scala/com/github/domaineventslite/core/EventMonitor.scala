package com.github.domaineventslite.core

class EventMonitor(eventStore: EventStore, eventPublisher: EventPublisher) {


  def publishEvent(event: DomainEvent): Unit = {
    eventPublisher.publish(event)
    eventStore.markAsPublished(event)
  }

  def publish(): Unit = {
    val unpublishedEvents = eventStore.unpublishedEvents()
    unpublishedEvents.foreach(event => publishEvent(event))
  }

}
