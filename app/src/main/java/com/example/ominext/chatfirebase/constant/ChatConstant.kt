package com.example.ominext.plaidfork.ui.chat

/**
 * Created by Ominext on 8/1/2017.
 */


object ChatConstant {

    //    public static final String SERVER_URL = "http://prevalentaugustus-31067.rhcloud.com/";
    val SERVER_URL = "http://192.168.137.1:3000/"

    //Event START
    val EVENT_SETUP_CONTACT = "setup_contacts"
    val EVENT_SEND_SUCCESSFULLY = "send_successfully"
    val EVENT_TYPING = "typing"
    val EVENT_USER_DISCONNECT = "user_disconnect"
    val EVENT_USER_CONNECT = "user_connected"

    //Event END

    //Key data START
    val _ID = "_id"
    val _ID_SENDER = "_id_sender"
    val _ID_RECEIVER = "_id_receiver"
    val MESSAGE = "message"
    val _ID_MESSAGE_CLIENT = "id_message_client"
    val CREATED_AT = "created_at"
    val IS_TYPING = "is_typing"
    val _ID_CONVERSATION = "_id_conversation"
    val MESSAGE_TYPE = "message_type"

    //Key data END

    val STATUS_ONLINE = 1
    val STATUS_OFFLINE = 2
    val STATUS_UNDEFINED = 3

    val TYPING = 0
    val PENDING = 1
    val SERVER_RECEIVED = 2
    val FRIEND_RECEIVED = 3
    val HANDLE_COMPLETE = 4

    //chat item
    val SINGLE = 0
    val FIRST = 1
    val MIDDLE = 2
    val LAST = 3

    val ITEM_MESSAGE_PER_PAGE = 30
    val TIME_DISTANCE: Long = 300000

    val MSG_TYPE_TEXT = 1
    val MSG_TYPE_ICON_HEART = 2
    val MSG_TYPE_MEDIA = 3

    val NOTIFICATION_ID = 125812


    //
    val USERS = "users"
    val USER = "user"
    val CONVERSATIONS = "CONVERSATIONS"
    val STATUS = "STATUS"
}
