package ch.ntruessel.broccoli.server.infrastructure.eventbus

import ch.ntruessel.broccoli.server.domain.Event
import ch.ntruessel.broccoli.server.domain.RecipeCreated
import ch.ntruessel.broccoli.server.domain.RecipeDeleted
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object EventMapper {

    private val objectMapper = jacksonObjectMapper()

    fun serialize(event: Event) =
        when (event) {
            is RecipeCreated -> SerializationResult(Types.RECIPE_CREATED, event)
            is RecipeDeleted -> SerializationResult(Types.RECIPE_DELETED, event)
        }

    fun deserialize(type: String, data: String) =
        when (Types.valueOf(type)) {
            Types.RECIPE_CREATED -> objectMapper.readValue<RecipeCreated>(data)
            Types.RECIPE_DELETED -> objectMapper.readValue<RecipeDeleted>(data)
        }

    enum class Types {
        RECIPE_CREATED,
        RECIPE_DELETED
    }

    data class SerializationResult (val type: String, val data: String) {
        constructor(type: Types, data: Event) : this(type.name, objectMapper.writeValueAsString(data))
    }
}
