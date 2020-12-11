package trashbet

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import java.io.File
import java.util.*

fun Route.userController(userService: UserService) {
    route("/user") {
        get("/") {
            val users = userService.getAllUsers()
            call.respond(users)
        }

        get("/me") {
            val principal = call.getUserPrincipal()!!
            val user = userService.getUserByName(principal.name)
            user?.let { call.respond(HttpStatusCode.OK, user) }
        }
    }
}

fun Route.betController(betService: BetService, wagerService: WagerService) {
    route("/bet") {
        adminRequired {
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

    route("/bet/{betId}/wager") {
        get("/") {
            val betId = call.parameters["betId"]!!
            val betUUID = UUID.fromString(betId)
            val wagers = wagerService.getWagersByBetId(betUUID)
            call.respond(wagers)
        }

        post("/") {
            val betId = call.parameters["betId"]!!
            val betUUID = UUID.fromString(betId)
            var wager = call.receive<Wager>()
            if (betUUID != wager.betId) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val userId = call.getUserPrincipal()?.id!!
            wager = wagerService.addWagerForUser(wager, userId)
            call.respond(HttpStatusCode.Created, wager)
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

        post("/login") {
            val creds = call.receive<UserRegistration>()
            val user = userService.loginUser(creds.username, creds.password)
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }
            call.sessions.set(UserPrincipal(user.id!!, user.name, isAdmin = false))
            call.respond(HttpStatusCode.Accepted)
        }
    }
}

fun Route.webController() {
    static("/") {
        staticRootFolder = File("web/public")
        file("build/bundle.js")
        file("build/bundle.css")
        file("build/bundle.js.map")
        file("global.css")
        file("index.html")
        file("favicon.png")
        default("index.html")
    }
}