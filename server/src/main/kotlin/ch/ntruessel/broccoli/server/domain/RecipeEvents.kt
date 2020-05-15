package ch.ntruessel.broccoli.server.domain

sealed class RecipeEvent : Event {
    abstract override val aggregateId: RecipeId
}

data class RecipeCreated(override val aggregateId: RecipeId) : RecipeEvent() { override val version = 0 }
data class RecipeDeleted(override val aggregateId: RecipeId, override val version: Number) : RecipeEvent()
