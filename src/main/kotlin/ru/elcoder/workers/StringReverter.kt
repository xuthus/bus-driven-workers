package ru.elcoder.workers

import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import ru.elcoder.bus.EventBus
import ru.elcoder.bus.EventHandler
import ru.elcoder.messages.Message
import ru.elcoder.messages.MessageResult
import ru.elcoder.messages.MessageType

private val log = LogFactory.getLog(StringReverter::class.java)

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class StringReverter(private val eventBus: EventBus) {

    @EventHandler(messageType = MessageType.STRING_READ)
    fun reverseString(message: Message): MessageResult {
        log.debug("reverseString: $message")
        val result = message.payload.toString().reversed()
        log.debug("reversed string: $result")
        eventBus.publish(Message(MessageType.WRITE, message.source, result))
        return MessageResult.NOTIFIED  // will be many instances
    }

}