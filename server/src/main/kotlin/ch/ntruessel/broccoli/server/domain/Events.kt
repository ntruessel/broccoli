package ch.ntruessel.broccoli.server.domain

sealed class Event {
    abstract val aggregateId: Id
    abstract val version: Int
}

sealed class RecipeEvent : Event() {
    abstract override val aggregateId: RecipeId
}

data class RecipeCreated(override val aggregateId: RecipeId) : RecipeEvent() { override val version = 0 }
data class RecipeDeleted(override val aggregateId: RecipeId, override val version: Int) : RecipeEvent()
