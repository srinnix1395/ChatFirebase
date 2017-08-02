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

//    @SerializedName("_id_conversation")
    var conversationId: String? = null

//    @SerializedName("message")
    var message: String? = null

//    @SerializedName("message_type")
    var messageType: Int = 0

//    @SerializedName("created_at")
    var createdAt: Long = 0

//    @SerializedName("status")
    var status: Int = 0

//    @SerializedName("is_seen")
    var isSeen: Boolean = false

    var isTypingMessage: Boolean = false

    constructor() {
        messageType = ChatConstant.MSG_TYPE_TEXT
    }

    constructor(id: String, idSender: String, idReceiver: String, message: String, createdAt: Long, status: Int, isTypingMessage: Boolean) {
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
