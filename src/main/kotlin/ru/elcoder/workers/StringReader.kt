package ru.elcoder.workers

import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.elcoder.bus.EventBus
import ru.elcoder.messages.Message
import ru.elcoder.messages.MessageType
import java.io.File

private val log = LogFactory.getLog(StringReader::class.java)

@Component
class StringReader(private val eventBus: EventBus) {
    @Value("\${strings.file}")
    private lateinit var stringsFile: String

    fun run() {
        log.debug("StringReader.run(): file=$stringsFile")
        File("$stringsFile.src").forEachLine { line ->
            eventBus.publish(Message(MessageType.STRING_READ, stringsFile, line))
        }
    }
}