package trashbet

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.controllers() {
    route("/user") {
        get("/") {
            val users = transaction {
                Users.selectAll().map{ Users.toUser(it) }
            }
            call.respond(users)
        }

        post("/") {
            val user = call.receive<User>()
            transaction {
                Users.insert {
                    it[name] = user.name
                }
            }
            call.respond(user)
        }
    }
}