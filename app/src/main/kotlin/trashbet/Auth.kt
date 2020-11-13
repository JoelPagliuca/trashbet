package trashbet

import io.ktor.application.*
import io.ktor.auth.*
import java.lang.NumberFormatException
import java.util.*

data class UserPrincipal(val user: User) : Principal

fun Application.installAuth() {
    install(Authentication) {
        basic("mock") {
            realm = "ktor"
            validate { credentials ->
                try {
                    val amount = credentials.password.toInt()
                    val user = User(UUID.randomUUID(), credentials.name, amount)
                    UserPrincipal(user)
                }
                catch (e: NumberFormatException) {
                    null
                }
            }
        }

        basic("real") {
            realm = "ktor"
            validate { credentials ->
                val user = UserService().loginUser(credentials.name, credentials.password)
                if (user != null) {
                    UserPrincipal(user)
                } else {
                    null
                }
            }
        }
    }
}