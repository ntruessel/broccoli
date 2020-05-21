package ch.ntruessel.broccoli.server.infrastructure.readmodel

import ch.ntruessel.broccoli.server.domain.RecipeCreated
import ch.ntruessel.broccoli.server.domain.RecipeDeleted
import ch.ntruessel.broccoli.server.domain.RecipeId
import io.micronaut.runtime.event.annotation.EventListener
import io.micronaut.scheduling.annotation.Async
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Singleton

@Singleton
open class RecipeViewRepository {

    private val recipes = ConcurrentHashMap<RecipeId, RecipeView>()

    fun allRecipes(): Collection<RecipeView> {
        return recipes.values
    }

    @Async
    @EventListener
    open fun onRecipeCreated(event: RecipeCreated) {
        recipes[event.aggregateId] = RecipeView(event.aggregateId.id)
    }

    @Async
    @EventListener
    open fun onRecipeDeleted(event: RecipeDeleted) {
    }
}
