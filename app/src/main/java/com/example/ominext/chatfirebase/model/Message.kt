package com.example.ominext.chatfirebase.model

import com.example.ominext.chatfirebase.constant.ChatConstant
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


/**
 * Created by Ominext on 8/1/2017.
 */

@IgnoreExtraProperties
class Message {

    var id: String? = null

    var idSender: String? = null

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

    constructor(id: String, idSender: String, message: String, messageType: String, createdAt: Long, status: String, isSeen: Boolean) {
        this.id = id
        this.idSender = idSender
        this.message = message
        this.messageType = messageType
        this.createdAt = createdAt
        this.status = status
        this.isSeen = isSeen
    }

    constructor(messageSample: Message) {
        this.id = messageSample.id
        this.idSender = messageSample.idSender
        this.message = messageSample.message
        this.createdAt = messageSample.createdAt
        this.status = messageSample.status
    }

    fun copy(messageSample: Message) {
        this.id = messageSample.id
        this.idSender = messageSample.idSender
        this.message = messageSample.message
        this.messageType = messageSample.messageType
        this.createdAt = messageSample.createdAt
        this.status = messageSample.status
    }

    @Exclude
    fun toMap(): Map<String, Any?> {
        val result = HashMap<String, Any?>()
        result.put(ChatConstant._ID, id)
        result.put(ChatConstant.ID_SENDER, idSender)
        result.put(ChatConstant.MESSAGE, message)
        result.put(ChatConstant.MESSAGE_TYPE, messageType)
        result.put(ChatConstant.CREATED_AT, createdAt)
        result.put(ChatConstant.STATUS, status)
        result.put(ChatConstant.IS_SEEN, isSeen)

        return result
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