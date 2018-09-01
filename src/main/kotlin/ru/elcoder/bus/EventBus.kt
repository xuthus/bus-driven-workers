package ru.elcoder.bus

import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Component
import ru.elcoder.messages.Message
import ru.elcoder.messages.MessageResult
import ru.elcoder.messages.MessageType
import java.lang.reflect.Method
import java.util.*

private val log = LogFactory.getLog(EventBus::class.java)

@Component
class EventBus {

    private val subscribers: MutableList<Subscriber> = ArrayList()

    fun publish(message: Message) {
        log.debug("publishing: $message")
        var handled = false
        subscribers.forEach { s ->
            if (s.messageType == message.messageType) {
                handled = true
                if (s.notify(message) == MessageResult.HANDLED) {
                    return
                }
            }
        }
        if (!handled) {
            throw EventBusException("Message with type " + message.messageType + " was not handled")
        }
    }

    fun subscribe(subscriber: Any) {
        log.debug("subscribing: ${subscriber.javaClass.simpleName}")
        subscriber.javaClass.methods.forEach { method ->
            if (method.getAnnotation(EventHandler::class.java) != null) {
                validateMethod(method)
                val handlerAnnotation = method.getAnnotation(EventHandler::class.java)
                subscribers.add(Subscriber(subscriber, method, handlerAnnotation.messageType))
            }
        }
    }

    private fun validateMethod(method: Method?) {
        if (method == null) {
            throw IllegalArgumentException("Subscriber must have method marked with @EventHandler annotation")
        }
        if (method.returnType != MessageResult::class.java) {
            throw IllegalArgumentException("@EventHandler method must have Boolean return type")
        }
        if (method.parameterCount != 1 || method.parameters[0].type != Message::class.java) {
            throw IllegalArgumentException("@EventHandler method must have exactly one argument of type Message")
        }
    }

    private data class Subscriber(val target: Any, val method: Method, val messageType: MessageType) {
        // here we can control further message processing
        fun notify(message: Message): MessageResult = method.invoke(target, message) as MessageResult
    }

}