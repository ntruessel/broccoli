package ch.ntruessel.broccoli.server.application

import ch.ntruessel.broccoli.server.domain.RecipeCreated
import ch.ntruessel.broccoli.server.domain.RecipeId
import org.springframework.stereotype.Service

@Service
class RecipeCommands(
        val eventStore: EventStore
) {
    fun createRecipe() {
        eventStore.commit(listOf(RecipeCreated(RecipeId())))
    }
}
