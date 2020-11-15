package trashbet

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.userController(userService: UserService) {
    route("/user") {
        get("/") {
            val users = userService.getAllUsers()
            call.respond(users)
        }

        get("/me") {
            val principal = call.authentication.principal<UserPrincipal>()
            principal?.user?.let { u -> call.respond(u) }
        }
    }
}

fun Route.betController() {
    route("/bet") {
        get("/") {
            val bets = transaction {
                Bets.selectAll().map{ Bets.toBet(it) }
            }
            call.respond(bets)
        }

        post("/") {
            val bet = call.receive<Bet>()
            val id = transaction {
                Bets.insert {
                    it[description] = bet.description
                    it[complete] = false
                } get Bets.id
            }
            val output = transaction {
                Bets.select {
                    (Bets.id eq id)
                }.mapNotNull {
                    Bets.toBet(it)
                }.singleOrNull()
            }
            if (output == null) call.respond(HttpStatusCode.BadRequest)
            else call.respond(HttpStatusCode.Created, output)
        }
    }
}

fun Route.unauthedControllers(userService: UserService) {
    route("/") {
        get("/health") {
            call.respondText("healthy")
        }

        post("/register") {
            val registration = call.receive<UserRegistration>()
            var user = User(name=registration.username, amount=0)
            user = userService.addUser(user, registration.password)
            call.respond(user)
        }
    }
}