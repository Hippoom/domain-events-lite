package com.github.domaineventslite.core

import org.joda.time.DateTime

case class DomainEvent(sequence: String, when: DateTime, eventPayload: Any) {

}
