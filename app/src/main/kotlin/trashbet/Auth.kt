package trashbet

import io.ktor.auth.*

data class UserPrincipal(val user: User) : Principal

fun Authentication.Configuration.installAuth() {
    basic("mock") {
        realm = "ktor"
        validate { credentials ->
            val user = UserService().getUserByName(credentials.name)
            if (user == null) user
            else UserPrincipal(user)
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