package com.github.domaineventslite.core

trait EventStore {
  def markAsPublished(event: DomainEvent): Unit

  def unpublishedEvents(): Seq[DomainEvent]

  def append(domainEvent: Any): Unit

}
