package trashbet

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.netty.Netty
import io.ktor.server.engine.embeddedServer
import kotlinx.serialization.json.Json


fun Application.myapp() {
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
    
    transaction {
        SchemaUtils.create(Users)
        SchemaUtils.create(Bets)

        Users.insert { 
            it[name] = "John"
            it[amount] = 20
        }
        Users.insert { 
            it[name] = "Jack"
            it[amount] = 20
        }
        Users.insert { 
            it[name] = "Jill"
            it[amount] = 20
        }
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
        module = Application::myapp,
        port = 8080
    ).start(wait=true)
}
