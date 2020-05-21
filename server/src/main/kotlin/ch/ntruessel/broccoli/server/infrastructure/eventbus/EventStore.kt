package ch.ntruessel.broccoli.server.infrastructure.eventbus

import ch.ntruessel.broccoli.server.domain.Event
import ch.ntruessel.broccoli.server.jooq.tables.Events.EVENTS
import ch.ntruessel.broccoli.server.jooq.tables.Version.VERSION
import io.micronaut.context.event.ApplicationEventPublisher
import org.jooq.DSLContext
import org.jooq.JSON
import org.jooq.impl.DSL
import java.util.*
import javax.inject.Singleton

@Singleton
class EventStore(
        val dslContext: DSLContext,
        val eventBus: ApplicationEventPublisher
) {

    fun save(event: Event) {
        return save(listOf(event))
    }

    fun save(events: List<Event>) {
        val transactionId = UUID.randomUUID()
        val expectedVersions = events.groupingBy { event -> event.aggregateId }
                .aggregate { _, accumulator: Int?, event, first ->
                    if (first) {
                        event.version
                    } else {
                        if (accumulator != event.version) {
                            // Abort - Inconsistent Events
                            TODO()
                        } else {
                            accumulator
                        }
                    }
                }
                .mapKeys { entry -> entry.key.id }
        dslContext.connection { connection ->
            var context = DSL.using(connection)
            connection.autoCommit = false

            val versions = context.selectFrom(VERSION)
                    .where(VERSION.ID.`in`(expectedVersions.keys))
                    .forUpdate()
                    .fetch()
            versions.forEach { record ->
                if (expectedVersions[record.id] != record.version) {
                    connection.rollback()
                    TODO()
                }
            }
            versions.forEach { record ->
                record.version = record.version + 1
            }
            context.batchStore(versions)
            val insert = context.insertInto(EVENTS, EVENTS.TYPE, EVENTS.DATA, EVENTS.TRANSACTION)
            events.forEach { event ->
                val (type, data) = EventMapper.serialize(event)
                insert.values(type, JSON.valueOf(data), transactionId)
            }
            insert.execute()
        }
        events.forEach { event -> eventBus.publishEvent(event) }
    }
}
