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
import java.util.*
import kotlin.test.*

class AppTest {

    var bet1Id: UUID = UUID.randomUUID()
    val basicAuth = "am9lbDpqb2Vs"

    @Test
    fun testHealthCheck() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "/health")) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun testUsers() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "/user", setup={addHeader("Authorization", "Basic $basicAuth")})) {
            assertNotNull(response.content)
            val users = Json.decodeFromString<List<User>>(response.content ?: "")
            assertEquals(1, users.size)
        }
        with(handleRequest(HttpMethod.Get, "/user/me", setup={addHeader("Authorization", "Basic $basicAuth")})) {
            val user = Json.decodeFromString<User>(response.content ?: "")
            assertEquals(user.name, "joel")
        }
    }

    @Test
    fun testRegistration() = withTestApplication(Application::main) {
        // make a user
        with(handleRequest(HttpMethod.Post, "/register", setup = {
            setBody("{\"username\":\"test\", \"password\":\"test\"}")
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
            addHeader("Authorization", "Basic $basicAuth")
        })) {
            assertNotNull(response.content)
            assertEquals(HttpStatusCode.Created, response.status())
            val bet = Json.decodeFromString<Bet>(response.content ?: "")
            assertNotNull(bet.id)
            assertFalse(bet.complete)
        }
    }

    @Test
    fun testWagers() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Post, "/bet/$bet1Id/wager", setup = {
            setBody(Json.encodeToString(Wager(amount = 15, outcome = false, userId = UUID.randomUUID(), betId = bet1Id)))
            addHeader("Content-Type", "application/json")
            addHeader("Authorization", "Basic $basicAuth")
        })) {
            assertEquals(HttpStatusCode.Created, response.status())
        }
        with(handleRequest(HttpMethod.Post, "/bet/$bet1Id/wager", setup = {
            setBody(Json.encodeToString(Wager(amount = 15, outcome = false, userId = UUID.randomUUID(), betId = bet1Id)))
            addHeader("Content-Type", "application/json")
            addHeader("Authorization", "Basic $basicAuth")
        })) {
            assertEquals(HttpStatusCode.BadRequest, response.status())
        }
    }

    @Test
    fun testSessions() = withTestApplication(Application::main) {
        var authCookie : String
        with(handleRequest(HttpMethod.Post, "/login", setup = {
            setBody("{\"username\":\"joel\", \"password\":\"joel\"}")
            addHeader("Content-Type", "application/json")
        })) {
            assertNotNull(response.cookies[AUTH_COOKIE])
            authCookie = response.cookies[AUTH_COOKIE]!!.value
        }
        with(handleRequest(HttpMethod.Get, "/user/me", setup={addHeader("Cookie", "$AUTH_COOKIE=$authCookie")})) {
            assertEquals(HttpStatusCode.OK, response.status())
            val user = Json.decodeFromString<User>(response.content ?: "")
            assertEquals(user.name, "joel")
        }
    }

    @BeforeTest
    fun before() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(Bets)
            SchemaUtils.create(Wagers)
            Wagers.deleteAll()
            Users.deleteAll()
            Bets.deleteAll()

            Users.insert {
                it[name] = "joel"
                it[amount] = 20
                it[passwordHash] = UserService().hashPassword("joel")
            }

            val bet1 = Bets.insert {
                it[description] = "test bet"
                it[complete] = false
            } get Bets.id
            bet1Id = bet1.value
        }
    }
}
