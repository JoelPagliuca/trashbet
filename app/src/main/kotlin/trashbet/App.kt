package trashbet

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.netty.Netty
import io.ktor.server.engine.embeddedServer

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080) { 
        routing {
            get("/") {
                call.respondText("Hello, world!")
            }
        }
    }.start(wait=true)
}
