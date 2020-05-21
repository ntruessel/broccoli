package ch.ntruessel.broccoli.server.infrastructure.rest

import ch.ntruessel.broccoli.server.domain.RecipeCreated
import ch.ntruessel.broccoli.server.domain.RecipeId
import ch.ntruessel.broccoli.server.infrastructure.readmodel.RecipeView
import ch.ntruessel.broccoli.server.infrastructure.eventbus.EventStore
import ch.ntruessel.broccoli.server.infrastructure.readmodel.RecipeViewRepository
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post

@Controller("/recipes")
class RecipeController(
        private val eventStore: EventStore,
        private val recipeViewRepository: RecipeViewRepository
) {

    @Get
    fun recipes(): Collection<RecipeView> {
        return recipeViewRepository.allRecipes()
    }

    @Post()
    fun createRecipe() {
        eventStore.save(RecipeCreated(RecipeId()))
    }
}
