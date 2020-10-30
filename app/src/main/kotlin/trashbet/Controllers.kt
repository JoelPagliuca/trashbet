package trashbet

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.controllers() {
    route("/") {
        get("/health") {
            call.respondText("healthy")
        }
    }

    route("/user") {
        get("/") {
            val users = transaction {
                Users.selectAll().map{ Users.toUser(it) }
            }
            call.respond(users)
        }

        post("/") {
            var user = call.receive<User>()
            val id = transaction {
                Users.insert {
                    it[name] = user.name
                    it[amount] = user.amount
                }
            } get Users.id
            user = transaction {
                Users.select {
                    (Users.id eq id)
                }.mapNotNull {
                    Users.toUser(it)
                }.single()
            }
            call.respond(user)
        }
    }

    route("/bet") {

    }
}