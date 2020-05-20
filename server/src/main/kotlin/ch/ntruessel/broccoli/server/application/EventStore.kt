package ch.ntruessel.broccoli.server.application

import ch.ntruessel.broccoli.server.domain.Event

interface EventStore {
    fun save(events: List<Event>)
}

