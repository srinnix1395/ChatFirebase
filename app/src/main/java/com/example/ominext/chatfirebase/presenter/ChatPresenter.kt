package com.example.ominext.chatfirebase.presenter

import android.arch.lifecycle.LifecycleObserver
import android.content.Context
import android.os.Bundle
import com.example.ominext.chatfirebase.ChatApplication
import com.example.ominext.chatfirebase.constant.ChatConstant
import com.example.ominext.chatfirebase.model.*
import com.example.ominext.chatfirebase.util.DebugLog
import com.example.ominext.chatfirebase.util.Utils
import com.example.ominext.chatfirebase.util.toast
import com.example.ominext.chatfirebase.view.ChatFragment
import com.example.ominext.chatfirebase.widget.isNotExistIn
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
    var page: Int = FIRST_PAGE
    var isLoading: Boolean = false
    var hasNext: Boolean = true
    var isLoadedInitial: Boolean = false

    companion object {
        @JvmField val FIRST_PAGE = 1
    }

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
        addUserListener()
        addMessageListener()
    }

    private fun addUserListener() {

    }

    private fun addMessageListener() {
        conversationRef?.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                if (!isLoadedInitial) {
                    return
                }
                println("Child changed: ${p0?.value}")

                val message: Message? = p0?.getValue(Message::class.java)
                if (listMessage.isNotExistIn(message)) {
                    view?.insertMessage(message)
                } else {
                    view?.updateStatusMessage(message?.id, message?.createdAt)
                }
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                if (!isLoadedInitial) {
                    return
                }

                println("Child added: ${p0?.value}")
                val message: Message? = p0?.getValue(Message::class.java)
                if (listMessage.isNotExistIn(message)) {
                    view?.insertMessage(message)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {

            }
        })
    }

    fun onLoadMessage() {
        if (isLoading || !hasNext) return

        isLoading = true

        if (page > FIRST_PAGE) {
            view?.addLoadingType()
        } else {
            view?.showProgressBar(true)
        }

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

            override fun onDataChange(p0: DataSnapshot) {
                val messages = arrayListOf<Any>()
                var addedItemsCount = 0
                val childrenCount = p0.childrenCount.toInt()

                if (page > FIRST_PAGE && childrenCount == 1) {
                    hasNext = false
                    isLoading = false
                    isLoadedInitial = true
                    return
                }

                Observable.fromIterable(p0.children)
                        .map { child ->
                            println(Thread.currentThread().name)

                            val value = child.getValue(Message::class.java)
                            messages.add(value!!)
                            addedItemsCount++

                            return@map value
                        }
                        .scan(Message(), { t1, t2 ->
                            val isLastItem = (addedItemsCount == childrenCount)
                            if (isLastItem) {
                                return@scan t2
                            }

                            if (t2.createdAt - t1.createdAt >= ChatConstant.TIME_DISTANCE) {
                                messages.add(messages.size - 1, t2.createdAt)
                            }
                            return@scan t2
                        })
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally {
                            hasNext = addedItemsCount == ChatConstant.ITEM_MESSAGE_PER_PAGE
                            page++
                            isLoading = false
                            isLoadedInitial = true
                        }
                        .subscribe({
                            //onNext: do nothing
                        }, {
                            //onError:

                        }, {
                            //onComplete
                            pivotMessageId = (messages[1] as Message).id

                            if (page > FIRST_PAGE) {
                                messages.removeAt(messages.size - 1)
                            }

                            if (page == FIRST_PAGE) {
                                view?.showProgressBar(false)
                            } else if (listMessage.isNotEmpty() && listMessage.last() is LoadingItem) {
                                view?.removeLoadingItem(0)
                            }

                            if (listMessage.isNotEmpty() && listMessage.first() is Long) {
                                if (listMessage.first() as Long - (messages.last() as Message).createdAt < ChatConstant.TIME_DISTANCE) {
                                    view?.removeItem(0)
                                }
                            }

                            view?.addMessage(messages, 0, page)
                        })
            }
        })
    }

    fun sendMessage(context: Context, text: String, typeMessage: Int) {
        if (!Utils.isNetworkAvailable(context)) {
            toast("No internet connection")
            return
        }

        val message: Message = Message()
        message.id = conversationRef?.push()?.key
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

        conversationRef?.child(message.id)?.setValue(message)
    }
}
