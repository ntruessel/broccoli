package ch.ntruessel.broccoli.server.infrastructure.eventbus

import ch.ntruessel.broccoli.server.application.EventStore
import ch.ntruessel.broccoli.server.domain.Event
import ch.ntruessel.broccoli.server.jooq.tables.Events.EVENTS
import ch.ntruessel.broccoli.server.jooq.tables.Version.VERSION
import org.jooq.DSLContext
import org.jooq.JSON
import org.springframework.stereotype.Component
import java.util.*

@Component
class JdbcEventStore(
        val dslContext: DSLContext
): EventStore {

    override fun save(events: List<Event>) {
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
        dslContext.transaction { ->
            val versions = dslContext.selectFrom(VERSION)
                    .where(VERSION.ID.`in`(expectedVersions.values))
                    .forUpdate()
                    .fetch()
            versions.forEach { record ->
                if (expectedVersions[record.id] != record.version) {
                    // Abort - Concurrent Modification
                    TODO()
                }
            }
            versions.forEach { record ->
                record.version = record.version + 1
            }
            dslContext.batchStore(versions)
            val insert = dslContext.insertInto(EVENTS, EVENTS.TYPE, EVENTS.DATA, EVENTS.TRANSACTION)
            events.forEach { event ->
                val (type, data) = EventMapper.serialize(event)
                insert.values(type, JSON.valueOf(data), transactionId)
            }
            insert.execute()
        }
    }
}
