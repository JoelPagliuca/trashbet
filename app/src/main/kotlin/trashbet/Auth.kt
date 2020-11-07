package trashbet

import io.ktor.application.*
import io.ktor.auth.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
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
                try {
                    val user = transaction {
                        Users.select {
                            // not real auth yet, still thinking
                            (Users.name eq credentials.name)
                        }.mapNotNull {
                            Users.toUser(it)
                        }.single()
                    }
                    UserPrincipal(user)
                }
                catch (e: Exception) {
                    Unit
                }
                null
            }
        }
    }
}