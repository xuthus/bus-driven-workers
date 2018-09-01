package ru.elcoder.messages

enum class MessageResult {
    HANDLED,  // message does not require further processing
    NOTIFIED  // message requires further processing
}
