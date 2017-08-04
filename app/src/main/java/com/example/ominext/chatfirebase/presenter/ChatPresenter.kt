package com.example.ominext.chatfirebase.presenter

import android.os.Bundle
import com.example.ominext.chatfirebase.ChatApplication
import com.example.ominext.chatfirebase.model.User
import com.example.ominext.plaidfork.ui.chat.ChatConstant
import com.example.ominext.plaidfork.ui.chat.Message
import com.example.ominext.plaidfork.ui.chat.view.ChatFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import java.util.*

/**
 * Created by Ominext on 8/2/2017.
 */
class ChatPresenter {
    var view: ChatFragment? = null

    val listMessage: ArrayList<Any> = ArrayList()
    var urlImage: String? = null
    var currentUser: FirebaseUser? = null
    var db:DatabaseReference? = null
    var userFriend: User? = null
    lateinit var conversationKey: String

    fun addView(chatFragment: ChatFragment) {
        view = chatFragment
        db = ChatApplication.app?.db
        currentUser = FirebaseAuth.getInstance().currentUser
    }

    fun onLoadMessage() {
        db?.child(ChatConstant.CONVERSATIONS)?.child(conversationKey)
    }

    fun getData(arguments: Bundle) {
        userFriend = arguments.getParcelable(ChatConstant.USER)
        if (currentUser?.uid?.compareTo(userFriend?.uid!!)!! > 0) {
            conversationKey = currentUser?.uid + userFriend?.uid
        } else {
            conversationKey = userFriend?.uid + currentUser?.uid
        }
    }

    fun sendMessage(text: String, typeMessage: Int) {
        val message: Message = Message()
        message.idSender = currentUser?.uid
        message.idReceiver = userFriend?.uid
        message.status = ChatConstant.PENDING

        if (typeMessage == 1) {
//            message.messageType = Type.LIKE
        }
    }

    private fun insertMessage(message: Message) {
        db?.child(ChatConstant.CONVERSATIONS)?.child(conversationKey)?.push()?.setValue(message)
    }
}
