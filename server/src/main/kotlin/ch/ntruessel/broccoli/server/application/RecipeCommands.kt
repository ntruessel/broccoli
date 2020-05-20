package ch.ntruessel.broccoli.server.application

import ch.ntruessel.broccoli.server.domain.RecipeCreated
import ch.ntruessel.broccoli.server.domain.RecipeId
import javax.inject.Singleton

@Singleton
class RecipeCommands(
        val eventStore: EventStore
) {
    fun createRecipe() {
        eventStore.save(listOf(RecipeCreated(RecipeId())))
    }
}
