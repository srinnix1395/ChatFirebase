package com.example.ominext.chatfirebase.presenter

import android.arch.lifecycle.LifecycleObserver
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.example.ominext.chatfirebase.ChatApplication
import com.example.ominext.chatfirebase.constant.ChatConstant
import com.example.ominext.chatfirebase.model.*
import com.example.ominext.chatfirebase.util.DebugLog
import com.example.ominext.chatfirebase.util.Utils
import com.example.ominext.chatfirebase.util.toast
import com.example.ominext.chatfirebase.view.ChatFragment
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.*

/**
 * Created by Ominext on 8/2/2017.
 */
class ChatPresenter : LifecycleObserver {
    var view: ChatFragment? = null

    val listMessage: ArrayList<Any?> = ArrayList()
    var conversationRef: DatabaseReference? = null

    var currentUser: FirebaseUser? = null
    var userFriend: User? = null

    lateinit var conversationKey: String
    var pivotMessageId: String? = null
    var page: Int = 1
    var isLoading: Boolean = false
    var hasNext: Boolean = true
    var isLoadInitial: Boolean = false

    fun addView(chatFragment: ChatFragment) {
        view = chatFragment
        currentUser = ChatApplication.app?.firebaseUser
    }

    fun getData(arguments: Bundle) {
        userFriend = arguments.getParcelable(ChatConstant.USER)
        if (currentUser?.uid?.compareTo(userFriend?.uid!!)!! > 0) {
            conversationKey = currentUser?.uid + userFriend?.uid
        } else {
            conversationKey = userFriend?.uid + currentUser?.uid
        }

        conversationRef = ChatApplication.app?.db?.child(ChatConstant.CONVERSATIONS)?.child(conversationKey)
        addListener()
    }

    fun onLoadMessage() {
        if (isLoading || !hasNext) return

        if (page > 1) {
            view?.addLoadingType()
        } else {
            view?.showProgressBar(true)
        }

        isLoading = true

        val query: Query? = conversationRef?.orderByKey()
        pivotMessageId?.let {
            query?.endAt(pivotMessageId)
        }

        query?.limitToLast(ChatConstant.ITEM_MESSAGE_PER_PAGE)?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                DebugLog.e(p0?.message ?: "null")
                toast("Get message failed")
                isLoading = false
            }

            override fun onDataChange(p0: DataSnapshot?) {
                DebugLog.i("Data changed: ${p0?.childrenCount ?: 0}")
                val messages = arrayListOf<Message>()
                p0?.children?.forEach { snapshot ->
                    val message = snapshot.getValue(Message::class.java)
                    messages.add(message!!)
                }

                val count = messages.size

                if (count > 0) {
                    pivotMessageId = messages[0].id
                    messages.removeAt(0)
                }

                if (page == 1) {
                    view?.showProgressBar(false)
                    view?.addMessage(messages, 0, page)
                } else {
                    if (listMessage.isNotEmpty() && listMessage.last() is LoadingItem) {
                        view?.removeLoadingItem(0)
                    }

                    view?.addMessage(messages, 0, page)
                }

                hasNext = count == ChatConstant.ITEM_MESSAGE_PER_PAGE
                page++
                isLoading = false
                isLoadInitial = true
            }
        })
    }

    private fun addListener() {
        conversationRef?.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                DebugLog.i("Child changed: ${p0?.value}")
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                if (!isLoadInitial) {
                    return
                }
//                DebugLog.i("Child added: ${p0?.value}")
//                val message: Message? = p0?.getValue(Message::class.java)
//                if (listMessage.isNotEmpty()) {
//                    val lastObject = listMessage.last()
//                    if (lastObject is Message && lastObject) {
//
//                    }
//                }
//                view?.insertMessage(message)
            }

            override fun onChildRemoved(p0: DataSnapshot?) {

            }
        })
    }

    fun sendMessage(context: Context, text: String, typeMessage: Int) {
        if (!Utils.isNetworkAvailable(context)) {
            toast("No internet connection")
            return
        }

        val message: Message = Message()
        message.id = System.currentTimeMillis().toString()
        message.idSender = currentUser?.uid
        message.idReceiver = userFriend?.uid
        message.status = StatusMessage.PENDING.name
        message.createdAt = System.currentTimeMillis()

        if (typeMessage == 0 || typeMessage == 1) {
            message.messageType = TypeMessage.LIKE.name
            message.message = null
        } else {
            message.messageType = TypeMessage.TEXT.name
            message.message = text.trim()
        }

        view?.insertMessage(message)

        val idMessage = conversationRef?.push()?.key

        conversationRef?.child(idMessage)?.setValue(message)?.addOnCompleteListener {
            conversationRef?.child(idMessage)?.child(ChatConstant._ID)?.setValue(idMessage)
            conversationRef?.child(idMessage)?.child(ChatConstant.STATUS)?.setValue(StatusMessage.COMPLETE.name)
            conversationRef?.child(idMessage)?.child(ChatConstant.CREATED_AT)?.setValue(ServerValue.TIMESTAMP)
            conversationRef?.child(idMessage)?.child(ChatConstant.CREATED_AT)?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {

                }

                override fun onDataChange(p0: DataSnapshot?) {
                    view?.updateStatusMessage(message, idMessage, p0?.value as Long)
                }
            })
        }?.addOnFailureListener {
            println("fail")
        }

    }
}
