package utils

import java.io.File
import java.time.LocalDateTime

class Logger(private val logFileName: String) {

    init {
        File(logFileName).writeText("Log started at ${LocalDateTime.now()}\n")
    }

    fun log(message: String) {
        println(message)
        File(logFileName).appendText("$message\n")
    }
}
