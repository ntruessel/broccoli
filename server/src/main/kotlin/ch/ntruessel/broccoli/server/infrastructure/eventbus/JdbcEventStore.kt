package ch.ntruessel.broccoli.server.infrastructure.eventbus

import ch.ntruessel.broccoli.server.application.EventStore
import ch.ntruessel.broccoli.server.domain.Event
import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component
class JdbcEventStore(
        val dslContext: DSLContext
): EventStore {

    override fun commit(events: List<Event>) {
        TODO()
    }
}
