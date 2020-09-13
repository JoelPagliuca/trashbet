package me.joelpagliuca.trashbet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TrashbetApplication

fun main(args: Array<String>) {
	runApplication<TrashbetApplication>(*args)
}
