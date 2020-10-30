package trashbet

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AppTest {
    @Test
    fun testHealthCheck() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "/health")) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun testUsers() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "/user")) {
            println(response.content)
            assertNotNull(response.content)
            val users = Json.decodeFromString<List<User>>(response.content ?: "")
            assertEquals(0, users.size)
        }
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
