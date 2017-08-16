package com.example.ominext.chatfirebase.model


/**
 * Created by Ominext on 8/1/2017.
 */
class Message {

    var id: String? = null

    var idSender: String? = null

    var message: String? = null

    var messageType: String = TypeMessage.TEXT.name

    var createdAt: Long = 0

    var status: String = StatusMessage.PENDING.name

    var isSeen: Boolean = false

    fun isTypingMessage(): Boolean {
        return messageType == TypeMessage.TYPING.name
    }

    fun copy(messageSample: Message) {
        this.id = messageSample.id
        this.idSender = messageSample.idSender
        this.message = messageSample.message
        this.messageType = messageSample.messageType
        this.createdAt = messageSample.createdAt
        this.status = messageSample.status
    }
}

enum class TypeMessage {
    LIKE,
    TEXT,
    MEDIA,
    TYPING
}

enum class StatusMessage {
    PENDING,
    COMPLETE
}