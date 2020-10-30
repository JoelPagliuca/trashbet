package trashbet

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

fun seedData() {
    transaction {
        SchemaUtils.create(Users)
        SchemaUtils.create(Bets)

        Users.insert {
            it[name] = "John"
            it[amount] = 20
        }
        Users.insert {
            it[name] = "Jack"
            it[amount] = 20
        }
        Users.insert {
            it[name] = "Jill"
            it[amount] = 20
        }
    }
}