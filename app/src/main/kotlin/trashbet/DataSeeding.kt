package trashbet

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

fun seedData() {
    transaction {
        val userService = UserService()
        SchemaUtils.create(Users)
        SchemaUtils.create(Bets)
        SchemaUtils.create(Wagers)

        Users.insert {
            it[name] = "John"
            it[amount] = 20
            it[passwordHash] = userService.hashPassword("John")
        }
        Users.insert {
            it[name] = "Jack"
            it[amount] = 20
            it[passwordHash] = userService.hashPassword("Jack")
        }
        Users.insert {
            it[name] = "Jill"
            it[amount] = 20
            it[passwordHash] = userService.hashPassword("Jill")
        }

        Bets.insert {
            it[description] = "Seeded incomplete bet"
            it[complete] = false
        }
        Bets.insert {
            it[description] = "Seeded complete bet"
            it[complete] = true
            it[outcome] = true
        }
    }
}