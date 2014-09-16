package com.github.domaineventslite.core

trait EventStore {

  def append(domainEvent: Any): Unit

}
