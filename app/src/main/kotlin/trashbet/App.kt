package trashbet

import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.request.receive
import io.ktor.serialization.*
import io.ktor.server.netty.Netty
import io.ktor.server.engine.embeddedServer
import kotlinx.serialization.json.Json


fun Application.myapp() {
    install(ContentNegotiation) { 
        register(ContentType.Application.Json, SerializationConverter(Json { prettyPrint = true }))
    }
    
    routing {
        get("/") {
            call.respond(User("John", 30))
        }

        post("/") {
            val user = call.receive<User>()
            call.respond(user)
        }
    }
}

@Suppress("UNUSED_PARAMETER")
fun main(args: Array<String>) {
    embeddedServer(
        Netty,
        watchPaths = listOf("trashbet"),
        module = Application::myapp,
        port = 8080
    ).start(wait=true)
}
