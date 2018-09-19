package ru.elcoder.workers

import org.apache.commons.logging.Log
import org.springframework.stereotype.Component
import ru.elcoder.bus.EventBus
import ru.elcoder.bus.EventHandler
import ru.elcoder.messages.Message
import ru.elcoder.messages.MessageResult
import ru.elcoder.messages.MessageType
import javax.annotation.PostConstruct

@Component
class Calculator(private val eventBus: EventBus) {
    private lateinit var log: Log

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