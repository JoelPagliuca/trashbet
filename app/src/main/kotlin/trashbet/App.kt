package trashbet

import org.jetbrains.exposed.sql.*
import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.netty.Netty
import io.ktor.server.engine.embeddedServer
import kotlinx.serialization.json.Json


fun Application.main() {
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", "org.h2.Driver")

    val seed = System.getenv("APP_SEED") ?: "true"

    if (seed.toBoolean()) {
        seedData()
    }

    install(ContentNegotiation) { 
        register(ContentType.Application.Json, SerializationConverter(Json { prettyPrint = true }))
    }

    install(Routing) {
        controllers()
    }
}

@Suppress("UNUSED_PARAMETER")
fun main(args: Array<String>) {
    embeddedServer(
        Netty,
        watchPaths = listOf("trashbet"),
        module = Application::main,
        port = 8080
    ).start(wait=true)
}
