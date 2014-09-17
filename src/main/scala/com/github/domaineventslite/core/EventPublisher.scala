package com.github.domaineventslite.core

trait EventPublisher {
  def publish(event: DomainEvent)
}
