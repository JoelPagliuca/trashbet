package trashbet

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

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

fun Route.betController(betService: BetService) {
    route("/bet") {
        get("/") {
            val bets = betService.getAllBets()
            call.respond(bets)
        }

        post("/") {
            var bet = call.receive<Bet>()
            bet = betService.addBet(bet)
            call.respond(HttpStatusCode.Created, bet)
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