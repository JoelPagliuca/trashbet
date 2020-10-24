package trashbet

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.netty.Netty
import io.ktor.server.engine.embeddedServer

fun Application.myapp() {
    routing {
        get("/") {
            call.respondText("Now with reload")
        }
    }
}

fun main(args: Array<String>) {
    embeddedServer(
        Netty,
        watchPaths = listOf("trashbet"),
        module = Application::myapp,
        port = 8080
    ).start(wait=true)
}
