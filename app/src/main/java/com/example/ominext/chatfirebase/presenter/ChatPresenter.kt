package com.example.ominext.chatfirebase.presenter

import android.os.Bundle
import com.example.ominext.chatfirebase.model.User
import com.example.ominext.plaidfork.ui.chat.ChatConstant
import com.example.ominext.plaidfork.ui.chat.Message
import com.example.ominext.plaidfork.ui.chat.view.ChatFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

/**
 * Created by Ominext on 8/2/2017.
 */
class ChatPresenter {
    var view: ChatFragment? = null

    val listMessage: ArrayList<Any> = ArrayList()
    var urlImage: String? = null
    var currentUser: FirebaseUser? = null
    lateinit var db:DatabaseReference
    lateinit var userFriend: User
    lateinit var conversationKey: String

    fun addView(chatFragment: ChatFragment) {
        view = chatFragment
        db = FirebaseDatabase.getInstance().reference
        currentUser = FirebaseAuth.getInstance().currentUser
    }

    fun onLoadMessage() {

    }

    fun getData(arguments: Bundle) {
        userFriend = arguments.getParcelable(ChatConstant.USER)
        if (currentUser?.uid?.compareTo(userFriend.uid!!)!! > 0) {
            conversationKey = currentUser?.uid + userFriend.uid
        } else {
            conversationKey = userFriend.uid + currentUser?.uid
        }
    }

    fun sendMessage(text: String, typeMessage: Int) {
        if (typeMessage == 1) {

        }
    }

    private fun insertMessage(message: Message) {
        db.child(ChatConstant.CONVERSATIONS).child(conversationKey)
    }
}