package ru.elcoder.workers

import org.apache.commons.logging.Log
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.elcoder.bus.EventBus
import ru.elcoder.messages.Message
import ru.elcoder.messages.MessageType
import java.io.File

@Component
class ArrayReader(private val eventBus: EventBus) {
    @Value("\${numbers.file}")
    private lateinit var numbersFile: String
    private lateinit var log: Log

    fun run() {
        log.debug("ArrayReader.run(): file=$numbersFile")
        val numbersList = ArrayList<Int>()
        File("$numbersFile.src").forEachLine { line ->
            numbersList.add(line.toInt())
        }
        eventBus.publish(Message(MessageType.ARRAY_READ, numbersFile, numbersList.toIntArray()))
    }

}