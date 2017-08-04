package com.example.ominext.chatfirebase.presenter

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.example.ominext.chatfirebase.ChatApplication
import com.example.ominext.chatfirebase.model.LoadingItem
import com.example.ominext.chatfirebase.model.User
import com.example.ominext.plaidfork.ui.chat.*
import com.example.ominext.plaidfork.ui.chat.view.ChatFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.*

/**
 * Created by Ominext on 8/2/2017.
 */
class ChatPresenter {
    var view: ChatFragment? = null

    val listMessage: ArrayList<Any> = ArrayList()
    var conversationRef: DatabaseReference? = null

    var currentUser: FirebaseUser? = null
    var userFriend: User? = null
    lateinit var conversationKey: String
    var pivotMessageId: String? = null
    var page: Int = 1

    fun getData(arguments: Bundle) {
        userFriend = arguments.getParcelable(ChatConstant.USER)
        if (currentUser?.uid?.compareTo(userFriend?.uid!!)!! > 0) {
            conversationKey = currentUser?.uid + userFriend?.uid
        } else {
            conversationKey = userFriend?.uid + currentUser?.uid
        }

        conversationRef = ChatApplication.app?.db?.child(ChatConstant.CONVERSATIONS)?.child(conversationKey)?.ref
    }

    fun addView(chatFragment: ChatFragment) {
        view = chatFragment
        currentUser = FirebaseAuth.getInstance().currentUser
    }

    fun onLoadMessage(context: Context) {
        if (pivotMessageId != null) {
            view?.addLoadingType()
        } else {
            view?.showProgressBar(true)
        }

        val query: Query? = conversationRef?.orderByKey()

        pivotMessageId?.let {
            query?.endAt(pivotMessageId)
        }

        conversationRef?.orderByKey()?.limitToLast(ChatConstant.ITEM_MESSAGE_PER_PAGE)?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                println(p0?.message)
                Toast.makeText(context, "Get message failed", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                val messages = arrayListOf<Message>()
                p0.children.forEach { snapshot ->
                    val message = snapshot.getValue(Message::class.java)
                    messages.add(message!!)
                }

                val count = messages.size

                val sizeList = listMessage.size

                if (count > 0) {
                    pivotMessageId = messages[0].id
                    messages.removeAt(0)

                    if (page == 1) {
                        view?.addMessage(messages, 0)
                        view?.showProgressBar(false)
                        view?.scrollToBottom()
                    } else {
                        view?.addMessage(messages, 1)
                    }
                    page++
                } else {
                    if (page == 1) {
                        view?.showProgressBar(false)
                    } else if (listMessage.isNotEmpty() && listMessage.last() is LoadingItem) {
                        listMessage.removeAt(sizeList - 1)
                        view?.removeLoadingType(sizeList - 1)
                    }
                }
            }
        })
    }

    fun sendMessage(context: Context, text: String, typeMessage: Int) {
        if (!Utils.isNetworkAvailable(context)) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
            return
        }

        val message: Message = Message()
        message.idSender = currentUser?.uid
        message.idReceiver = userFriend?.uid
        message.status = StatusMessage.COMPLETE.name
        message.createdAt = System.currentTimeMillis()

        if (typeMessage == 1) {
            message.messageType = TypeMessage.LIKE.name
            message.message = null
        } else {
            message.messageType = TypeMessage.TEXT.name
            message.message = text.trim()
        }

        val idMessage = conversationRef?.push()?.key
        message.id = idMessage
        conversationRef?.child(idMessage)?.setValue(message)

        view?.insertMessage(message)
    }
}
