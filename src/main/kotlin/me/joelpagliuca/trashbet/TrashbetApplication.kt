package me.joelpagliuca.trashbet

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TrashbetApplication

fun main(args: Array<String>) {
	Database.connect("jdbc:h2:mem:test", "org.h2.Driver")
	transaction {
		SchemaUtils.create(Users)
		SchemaUtils.create(Bets)
	}
	runApplication<TrashbetApplication>(*args)
}
