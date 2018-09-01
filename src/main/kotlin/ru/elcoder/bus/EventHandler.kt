package ru.elcoder.bus

import ru.elcoder.messages.MessageType

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class EventHandler(val messageType: MessageType)
