package ch.ntruessel.broccoli.server.infrastructure.rest

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/recipes")
class RecipeController {

    @Get
    fun demo() {}

}
