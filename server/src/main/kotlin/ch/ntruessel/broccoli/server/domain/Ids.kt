package ch.ntruessel.broccoli.server.domain

import java.util.*

sealed class Id {
    abstract val id: UUID
}
data class RecipeId(override val id: UUID = UUID.randomUUID()) : Id()
