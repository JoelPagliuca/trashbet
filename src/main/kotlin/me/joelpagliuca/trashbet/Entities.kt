package me.joelpagliuca.trashbet

import org.jetbrains.exposed.dao.id.UUIDTable

object Users: UUIDTable() {
    val name = varchar("name", 100)
}

object Bets: UUIDTable() {
    val prediction = text("description")
    val amount = integer("amount")
    val complete = bool("complete")
}