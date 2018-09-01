package ru.elcoder.messages

data class Message(
        val messageType: MessageType,
        val source: String,
        val payload: Any) {
}