package ch.ntruessel.broccoli.server.infrastructure.rest

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class Router {

    @Bean
    fun recipeRouter() = router {
        GET("/recipes").invoke { _ -> ServerResponse.ok().build() }
    }
}
