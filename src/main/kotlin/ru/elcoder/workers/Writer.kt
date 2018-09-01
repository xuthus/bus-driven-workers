package ru.elcoder.workers

import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Component
import ru.elcoder.bus.EventBus
import ru.elcoder.bus.EventHandler
import ru.elcoder.messages.Message
import ru.elcoder.messages.MessageResult
import ru.elcoder.messages.MessageType
import java.io.File
import javax.annotation.PostConstruct

private val log = LogFactory.getLog(Writer::class.java)

@Component
class Writer(private val eventBus: EventBus) {

    @EventHandler(MessageType.WRITE)
    fun handle(message: Message): MessageResult {
        log.debug("write results to file: ${message.source}.dst")
        File(message.source + ".dst").appendText(message.payload.toString() + "\n", Charsets.UTF_8)
        return MessageResult.HANDLED // or NOTIFIED if there will be many instances
    }

    @PostConstruct
    private fun init() = eventBus.subscribe(this)

}