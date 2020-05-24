package ch.ntruessel.broccoli.server.infrastructure.eventbus

import ch.ntruessel.broccoli.server.domain.RecipeCreated
import ch.ntruessel.broccoli.server.domain.RecipeDeleted
import ch.ntruessel.broccoli.server.domain.RecipeId
import ch.ntruessel.broccoli.server.infrastructure.ConcurrentModificationError
import io.kotlintest.assertSoftly
import io.kotlintest.assertions.arrow.either.shouldBeLeft
import io.kotlintest.matchers.string.shouldContainIgnoringCase
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import io.micronaut.test.annotation.MicronautTest
import java.lang.IllegalArgumentException
import java.lang.RuntimeException

@MicronautTest
internal class EventStoreTest(
        private val eventStore: EventStore
): StringSpec({

    "When the EventStore should persist conflicting sets of events then only one of them is persisted" {
        val id = RecipeId()
        val createEvent = RecipeCreated(id)
        val deletedEvent = RecipeDeleted(id, 1)

        eventStore.save(createEvent)
        eventStore.save(deletedEvent)

        val result = eventStore.save(deletedEvent)
        assertSoftly {
            result shouldBeLeft ConcurrentModificationError
        }
    }

    "When the EventStore gets inconsistent events then they are rejected" {
        val id = RecipeId()
        val events = listOf(RecipeCreated(id), RecipeDeleted(id, 1))
        val exception = shouldThrow<IllegalArgumentException> {
            eventStore.save(events)
        }
        exception.message shouldContainIgnoringCase "inconsistent"
    }

    "When the EventStore should create a duplicate entity it rejects the events" {
        val id = RecipeId()
        val event = RecipeCreated(id)

        eventStore.save(event)
        shouldThrow<RuntimeException> {
            eventStore.save(event)
        }
    }
})
