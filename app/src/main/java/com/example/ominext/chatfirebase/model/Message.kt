package com.example.ominext.plaidfork.ui.chat

/**
 * Created by Ominext on 8/1/2017.
 */


class Message {

    //    @SerializedName("_id")
    var id: String? = null

    //    @SerializedName("_id_sender")
    var idSender: String? = null

    //    @SerializedName("_id_receiver")
    var idReceiver: String? = null

    //    @SerializedName("message")
    var message: String? = null

    //    @SerializedName("message_type")
    var messageType: String = TypeMessage.TEXT.name

    //    @SerializedName("created_at")
    var createdAt: Long = 0

    //    @SerializedName("status")
    var status: String = StatusMessage.PENDING.name

    //    @SerializedName("is_seen")
    var isSeen: Boolean = false

    var isTypingMessage: Boolean = false

    constructor()

    constructor(id: String, idSender: String, idReceiver: String, message: String, createdAt: Long, status: String, isTypingMessage: Boolean) {
        this.id = id
        this.idSender = idSender
        this.idReceiver = idReceiver
        this.message = message
        this.createdAt = createdAt
        this.status = status
        this.isTypingMessage = isTypingMessage
    }

    constructor(messageSample: Message) {
        this.id = messageSample.id
        this.idSender = messageSample.idSender
        this.idReceiver = messageSample.idReceiver
        this.message = messageSample.message
        this.createdAt = messageSample.createdAt
        this.status = messageSample.status
        this.isTypingMessage = messageSample.isTypingMessage
    }
}

enum class TypeMessage {
    LIKE,
    TEXT,
    MEDIA
}

enum class StatusMessage {
    PENDING,
    COMPLETE
}