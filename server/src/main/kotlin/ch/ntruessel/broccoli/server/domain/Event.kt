package ch.ntruessel.broccoli.server.domain

interface Event {
    val aggregateId: Id
    val version: Number
}
