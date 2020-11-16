package trashbet

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import java.util.*

class UserService {
    fun getAllUsers(): List<User> = transaction {
        Users.selectAll().map{ toUser(it) }
    }

    private fun getUserByName(name: String): User? = transaction {
        Users.select {
            (Users.name eq name)
        }.mapNotNull {
            toUser(it)
        }.singleOrNull()
    }

    fun addUser(user: User, password: String): User {
        if (getUserByName(user.name) != null) {
            throw Exception("username already exists")
        }
        val name = transaction {
            Users.insert {
                it[name] = user.name
                it[amount] = 0
                it[passwordHash] = hashPassword(password)
            }
        } get Users.name
        return getUserByName(name)!!
    }

    fun loginUser(username: String, password: String): User? {
        val user = getUserByName(username) ?: return null
        val passwordHash = transaction {
            Users.select {
                (Users.id eq user.id)
            }.withDistinct().map {
                it[Users.passwordHash]
            }.single()
        }
        if (checkPassword(password, passwordHash)) {
            return user
        }
        return null
    }

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    private fun checkPassword(plaintext: String, passwordHash: String): Boolean {
        return BCrypt.checkpw(plaintext, passwordHash)
    }

    private fun toUser(row: ResultRow): User = User(
            id = row[Users.id].value,
            name = row[Users.name],
            amount = row[Users.amount],
    )
}

class BetService {
    fun getAllBets(): List<Bet> = transaction {
        Bets.selectAll().map{ toBet(it) }
    }

    private fun getBetById(id: UUID): Bet? = transaction {
        Bets.select {
            (Bets.id eq id)
        }.mapNotNull {
            toBet(it)
        }.singleOrNull()
    }

    fun addBet(bet: Bet): Bet = transaction {
        val id = Bets.insert {
            it[description] = bet.description
            it[complete] = false
        } get Bets.id
        getBetById(id.value)!!
    }

    private fun toBet(row: ResultRow): Bet = Bet(
            id = row[Bets.id].value,
            description = row[Bets.description],
            complete = row[Bets.complete],
            outcome = row[Bets.outcome],
    )
}

