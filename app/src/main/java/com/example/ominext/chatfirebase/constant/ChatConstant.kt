package com.example.ominext.chatfirebase.constant

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

    val TYPING = 0
    val PENDING = 1
    val COMPLETE = 2

    //chat item
    val SINGLE = 0
    val FIRST = 1
    val MIDDLE = 2
    val LAST = 3

    val ITEM_MESSAGE_PER_PAGE = 30
    val TIME_DISTANCE: Long = 300000

    val NOTIFICATION_ID = 125812


    //
    val _ID = "id"
    val USERS = "users"
    val USER = "user"
    val CONVERSATIONS = "conversations"
    val STATUS = "status"
    val LAST_ONLINE = "lastOnline"
    val CREATED_AT = "createdAt"
}
