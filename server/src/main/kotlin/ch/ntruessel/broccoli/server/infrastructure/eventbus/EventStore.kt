package ch.ntruessel.broccoli.server.infrastructure.eventbus

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import ch.ntruessel.broccoli.server.domain.Event
import ch.ntruessel.broccoli.server.domain.Id
import ch.ntruessel.broccoli.server.infrastructure.ConcurrentModificationError
import ch.ntruessel.broccoli.server.infrastructure.Error
import ch.ntruessel.broccoli.server.jooq.tables.Events.EVENTS
import ch.ntruessel.broccoli.server.jooq.tables.Version.VERSION
import io.micronaut.context.event.ApplicationEventPublisher
import org.jooq.DSLContext
import org.jooq.JSON
import org.jooq.impl.DSL
import java.lang.IllegalArgumentException
import java.sql.Connection
import java.util.*
import javax.inject.Singleton

@Singleton
class EventStore(
        private val dslContext: DSLContext,
        private val eventBus: ApplicationEventPublisher
) {

    fun save(event: Event): Either<Error, Unit> {
        return save(listOf(event))
    }

    fun save(events: List<Event>): Either<Error, Unit> {
        val expectedVersions = expectedVersions(events)
        return dslContext.connectionResult { connection ->
            connection.autoCommit = false
            updateVersions(expectedVersions, connection)
                    .map { storeEvents(events, connection) }
                    .map { connection.commit() }
        }.map {
            events.forEach { event -> eventBus.publishEvent(event) }
        }
    }

    private fun storeEvents(events: List<Event>, connection: Connection) {
        val transactionId = UUID.randomUUID()
        val context = DSL.using(connection)
        val insert = context.insertInto(EVENTS, EVENTS.TYPE, EVENTS.DATA, EVENTS.TRANSACTION)
        events.forEach { event ->
            val (type, data) = EventMapper.serialize(event)
            insert.values(type, JSON.valueOf(data), transactionId)
        }
        insert.execute()
    }

    private fun updateVersions(expectedVersions: Map<UUID, Int>, connection: Connection): Either<Error, Unit> {
        val context = DSL.using(connection)
        val newAggregateRoots = expectedVersions.filter { entry -> entry.value == 0 }
        if (context.selectCount().from(VERSION).where(VERSION.ID.`in`(newAggregateRoots.keys)).fetchOne(0, Int::class.java) > 0) {
            throw IllegalArgumentException("Some of the aggregate roots already exist")
        }
        val insert = context.insertInto(VERSION, VERSION.ID, VERSION.VERSION_)
        newAggregateRoots.forEach { entry ->
            insert.values(entry.key, entry.value)
        }
        insert.execute()
        val versions = context.selectFrom(VERSION)
                .where(VERSION.ID.`in`(expectedVersions.keys))
                .forUpdate()
                .fetch()
        assert(versions.size == expectedVersions.size)
        if (versions.any { record -> expectedVersions[record.id] != record.version }) {
            connection.rollback()
            return Left(ConcurrentModificationError)
        }
        versions.forEach { record ->
            record.version = record.version + 1
        }
        context.batchStore(versions)
        return Right(Unit)
    }

    private fun expectedVersions(events: List<Event>): Map<UUID, Int> {
        return events.groupingBy { event -> event.aggregateId }
                .aggregate<Event, Id, Int> { _, accumulator, event, first ->
                    if (first) {
                        event.version
                    } else {
                        if (accumulator != event.version) {
                            throw IllegalArgumentException("Inconsistent events provided")
                        } else {
                            accumulator
                        }
                    }
                }
                .mapKeys { entry -> entry.key.id }
    }
}
