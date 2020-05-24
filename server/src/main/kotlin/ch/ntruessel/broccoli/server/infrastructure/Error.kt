package ch.ntruessel.broccoli.server.infrastructure

sealed class Error

object ConcurrentModificationError: Error()
