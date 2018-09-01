package ru.elcoder.workers

import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Component
import ru.elcoder.bus.EventBus
import ru.elcoder.bus.EventHandler
import ru.elcoder.messages.Message
import ru.elcoder.messages.MessageResult
import ru.elcoder.messages.MessageType
import javax.annotation.PostConstruct

private val log = LogFactory.getLog(Calculator::class.java)

@Component
class Calculator(private val eventBus: EventBus) {

    @PostConstruct
    private fun init() = eventBus.subscribe(this)

    @EventHandler(messageType = MessageType.ARRAY_READ)
    fun calculate(message: Message): MessageResult {
        log.debug("calculate sum: $message")
        val numbers = message.payload as IntArray
        val result = numbers.sum()
        log.debug("result: $result")
        eventBus.publish(Message(MessageType.WRITE, message.source, result))
        return MessageResult.HANDLED
    }

}