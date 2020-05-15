package ch.ntruessel.broccoli.server.domain.writemodel

import ch.ntruessel.broccoli.server.domain.*

class Recipe constructor(events: Sequence<RecipeEvent>) {
    val id: RecipeId
    val deleted: Boolean

    init {
        var id: RecipeId? = null
        var deleted = false

        events.forEach { event ->
            when (event) {
                is RecipeCreated -> id = event.aggregateId
                is RecipeDeleted -> deleted = true
            }
        }

        this.id = id!!
        this.deleted = deleted
    }

    fun addTag(tag: Tag) {}
    fun removeTag(tag: Tag) {}
    fun edit(yield: Yield? = null, ingredients: List<Ingredient> = listOf(), instructions: List<String> = listOf()) {}
    fun delete() {}
}
