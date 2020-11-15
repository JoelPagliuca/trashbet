package trashbet

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class AppTest {
    @Test
    fun testHealthCheck() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "/health")) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun testUsers() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "/user", setup={addHeader("Authorization", "Basic am9lbDozMA==")})) {
            assertNotNull(response.content)
            val users = Json.decodeFromString<List<User>>(response.content ?: "")
            assertEquals(1, users.size)
        }
        with(handleRequest(HttpMethod.Get, "/user/me", setup={addHeader("Authorization", "Basic am9lbDozMA==")})) {
            val user = Json.decodeFromString<User>(response.content ?: "")
            assertEquals(user.name, "joel")
        }
    }

    @Test
    fun testRegistration() = withTestApplication(Application::main) {
        // make a user
        with(handleRequest(HttpMethod.Post, "/register", setup = {
            setBody("{\"username\":\"joel\", \"password\":\"joel\"}")
            addHeader("Content-Type", "application/json")
        })) {
            assertNotNull(response.content)
            val user = Json.decodeFromString<User>(response.content ?: "")
            assertNotNull(user.amount)
            assertNotNull(user.id)
        }
        Unit
    }

    @Test
    fun testBets() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Post, "/bet", setup = {
            setBody(Json.encodeToString(Bet(description = "test bet", complete = false)))
            addHeader("Content-Type", "application/json")
            addHeader("Authorization", "Basic am9lbDow")
        })) {
            assertNotNull(response.content)
            val bet = Json.decodeFromString<Bet>(response.content ?: "")
            assertNotNull(bet.id)
            assertFalse(bet.complete)
        }
    }

    @BeforeTest
    fun before() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(Bets)
            Users.deleteAll()

            Users.insert {
                it[name] = "Jack"
                it[amount] = 20
                it[passwordHash] = UserService().hashPassword("Jack")
            }

            Bets.insert {
                it[description] = "test bet"
                it[complete] = false
            }
        }
    }
}
