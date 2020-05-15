package ch.ntruessel.broccoli.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BroccoliServer

fun main(args: Array<String>) {
    runApplication<BroccoliServer>(*args)
}
