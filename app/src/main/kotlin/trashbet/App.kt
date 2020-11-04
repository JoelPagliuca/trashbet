package trashbet

import org.jetbrains.exposed.sql.*
import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.Netty
import kotlinx.serialization.json.Json


fun Application.main() {
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", "org.h2.Driver")

    @Suppress("EXPERIMENTAL_API_USAGE")
    val deployment_environment = environment.config.property("ktor.deployment.environment").getString()
    println(deployment_environment)

    when (deployment_environment) {
        "testing" -> Unit
        "development" -> {
            seedData()
        }
        "production" -> Unit
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
    embeddedServer(Netty, commandLineEnvironment(args)).start(wait=true)
}
