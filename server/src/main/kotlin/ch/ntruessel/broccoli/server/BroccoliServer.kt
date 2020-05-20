package ch.ntruessel.broccoli.server

import io.micronaut.runtime.Micronaut

object BroccoliServer {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("ch.ntruessel.broccoli")
                .mainClass(BroccoliServer.javaClass)
                .start()
    }
}

