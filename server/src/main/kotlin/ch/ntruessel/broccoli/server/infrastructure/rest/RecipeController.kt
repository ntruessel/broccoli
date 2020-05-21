package ch.ntruessel.broccoli.server.infrastructure.rest

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import java.util.*

@Controller("/recipes")
class RecipeController {

    @Get
    fun recipes() {}

    @Get("/{id}")
    fun recipe(id: UUID) {}

    @Post()
    fun create() {}

}
