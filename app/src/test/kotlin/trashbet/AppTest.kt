package trashbet

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
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
        // there are no users
        with(handleRequest(HttpMethod.Get, "/user", setup={addHeader("Authorization", "Basic am9lbDozMA==")})) {
            println(response.status())
            assertNotNull(response.content)
            val users = Json.decodeFromString<List<User>>(response.content ?: "")
            assertEquals(0, users.size)
        }
        val user1 = User(name="joel", amount=11)
        // make a user
        with(handleRequest(HttpMethod.Post, "/user", setup={
            setBody(Json.encodeToString(user1))
            addHeader("Content-Type", "application/json")
            addHeader("Authorization", "Basic am9lbDozMA==")
        })) {
            assertNotNull(response.content)
            val user = Json.decodeFromString<User>(response.content ?: "")
            assertEquals(11, user.amount)
            assertNotNull(user.id)
        }
        Unit
    }

    @BeforeTest
    fun before() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(Bets)
            Users.deleteAll()
        }
    }
}
