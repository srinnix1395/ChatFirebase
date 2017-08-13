package com.example.ominext.chatfirebase.model

/**
 * Created by Ominext on 8/1/2017.
 */


class Message {

    var id: String? = null

    var idSender: String? = null

    var idReceiver: String? = null

    var message: String? = null

    var messageType: String = TypeMessage.TEXT.name

    var createdAt: Long = 0

    var status: String = StatusMessage.PENDING.name

    var isSeen: Boolean = false

    val isTypingMessage: Boolean
        get() {
            return messageType == TypeMessage.TYPING.name
        }

    constructor()

    constructor(id: String, idSender: String, idReceiver: String, message: String, createdAt: Long, status: String) {
        this.id = id
        this.idSender = idSender
        this.idReceiver = idReceiver
        this.message = message
        this.createdAt = createdAt
        this.status = status
    }

    constructor(messageSample: Message) {
        this.id = messageSample.id
        this.idSender = messageSample.idSender
        this.idReceiver = messageSample.idReceiver
        this.message = messageSample.message
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