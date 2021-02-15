package trashbet

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import java.util.*

class UserService {
    fun getAllUsers(): List<User> = transaction {
        Users.selectAll().map{ toUser(it) }
    }

    fun getUserByName(name: String): User? = transaction {
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

    private fun toBet(row: ResultRow): Bet {
        val wagers = transaction {
            Wagers.select {
                (Wagers.bet eq row[Bets.id])
            }
        }
        val afor = wagers.filter { it[Wagers.outcome] }.fold(0){sum, it -> sum + it[Wagers.amount]}
        val against = wagers.filterNot { it[Wagers.outcome] }.fold(0){sum, it -> sum + it[Wagers.amount]}
        return Bet(
                id = row[Bets.id].value,
                description = row[Bets.description],
                complete = row[Bets.complete],
                outcome = row[Bets.outcome],
                amount_for = afor,
                amount_against = against,
        )
    }
}

class WagerService {
    fun getWagersByBetId(betId: UUID): List<Wager> = transaction {
        Wagers.select {
            (Wagers.bet eq betId)
        }.mapNotNull {
            toWager(it)
        }
    }

    fun getWagersByUserId(userId: UUID): List<Wager> = transaction {
        Wagers.select {
            (Wagers.user eq userId)
        }.mapNotNull {
            toWager(it)
        }
    }

    private fun getWagerById(id: UUID): Wager? = transaction {
        Wagers.select {
            (Wagers.id eq id)
        }.mapNotNull {
            toWager(it)
        }.singleOrNull()
    }

    fun addWagerForUser(wager: Wager, userId: UUID): Wager {
        val amount = transaction {
            Users.select {
                (Users.id eq userId)
            }.single()[Users.amount]
        }
        if (wager.amount > amount) {
            throw InputException("User didn't have enough to place that bet")
        }
        val betId = transaction {
            Bets.select {
                (Bets.id eq wager.betId) and
                (Bets.complete eq false)
            }.single()[Bets.id]
        }
        val wagerId = transaction {
            Users.update({Users.id eq userId}) {
                it[Users.amount] = amount - wager.amount
            }
            Wagers.insert {
                it[Wagers.amount] = wager.amount
                it[outcome] = wager.outcome
                it[user] = EntityID<UUID>(userId, Users)
                it[bet] = betId
            } get Wagers.id
        }
        return getWagerById(wagerId.value)!!
    }

    private fun toWager(row: ResultRow): Wager = Wager(
            id = row[Wagers.id].value,
            amount = row[Wagers.amount],
            outcome = row[Wagers.outcome],
            userId = row[Wagers.user].value,
            betId = row[Wagers.bet].value,
    )
}