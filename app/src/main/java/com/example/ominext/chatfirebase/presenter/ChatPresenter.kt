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
import com.example.ominext.chatfirebase.widget.isExistIn
import com.example.ominext.chatfirebase.widget.isNotExistIn
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit

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
    val subjectTyping: PublishSubject<Boolean> = PublishSubject.create()
    var isUserTyping: Boolean = false
        set(value) {
            field = value
            conversationRef
                    ?.child(ChatConstant.TYPING_MESSAGE)
                    ?.child(currentUser?.uid)
                    ?.setValue(value)
        }

    companion object {
        @JvmField val FIRST_PAGE = 1
    }

    fun addView(chatFragment: ChatFragment) {
        view = chatFragment
        currentUser = ChatApplication.app?.firebaseUser

        subjectTyping
                .debounce(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    isUserTyping = false
                }
    }

    fun getData(arguments: Bundle) {
        userFriend = arguments.getParcelable(ChatConstant.USER)
        if (currentUser?.uid?.compareTo(userFriend?.uid!!)!! > 0) {
            conversationKey = currentUser?.uid + userFriend?.uid
        } else {
            conversationKey = userFriend?.uid + currentUser?.uid
        }

        conversationRef = ChatApplication.app?.db?.child(ChatConstant.CONVERSATIONS)?.child(conversationKey)

        addMessageListener()
        addUserListener()
        addTypingListener()
    }

    private fun addUserListener() {
        ChatApplication.app?.db?.child(ChatConstant.USERS)?.child(userFriend?.uid)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                DebugLog.e(p0?.message!!)
            }

            override fun onDataChange(data: DataSnapshot?) {
                val user = data?.getValue(User::class.java)
                user?.let {
                    view?.setStatus(user.status)
                }
            }
        })
    }

    private fun addTypingListener() {
        conversationRef?.child(ChatConstant.TYPING_MESSAGE)?.child(userFriend?.uid)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(data: DataSnapshot?) {
                val isFriendTyping = data?.getValue(Boolean::class.java)

                isFriendTyping?.let {
                    val isUserTypingChanged = listMessage.isNotEmpty()
                            && listMessage.last() is Message
                            && (listMessage.last() as Message).isTypingMessage != isFriendTyping

                    if (isUserTypingChanged) {
                        view?.showTypingMessage(isFriendTyping)
                    }
                }
            }
        })

        conversationRef
                ?.child(ChatConstant.TYPING_MESSAGE)
                ?.child(userFriend?.uid)
                ?.onDisconnect()
                ?.setValue(false)
    }

    private fun addMessageListener() {
        conversationRef?.child(ChatConstant.MESSAGES)
                ?.orderByChild(ChatConstant.CREATED_AT)
                ?.startAt(System.currentTimeMillis().toDouble())
                ?.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                //Child changed when message sent
                val message: Message? = p0?.getValue(Message::class.java)
                if (listMessage.isExistIn(message)) {
                    view?.updateStatusMessage(message?.id, message?.createdAt)
                }
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val message: Message? = p0?.getValue(Message::class.java)
                if (listMessage.isNotExistIn(message)) {
                    addMessage(message, false)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {

            }
        })
    }

    fun onUserTyping(text: String) {
        if (text.isEmpty() && isUserTyping) {
            isUserTyping = false
            return
        }

        if (!isUserTyping) {
            isUserTyping = true
        }
        subjectTyping.onNext(false)
    }

    fun onLoadMessage() {
        if (isLoading || !hasNext) return

        isLoading = true

        if (page > FIRST_PAGE) {
            view?.addLoadingItem()
        } else {
            view?.showProgressBar(true)
        }

        var query: Query? = conversationRef?.child(ChatConstant.MESSAGES)?.orderByKey()

        pivotMessageId?.let {
            query = query?.endAt(pivotMessageId)
        }

        query?.limitToLast(ChatConstant.ITEM_MESSAGE_PER_PAGE)?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                DebugLog.e(p0?.message ?: "null")
                toast("Get message failed")
                isLoading = false
            }

            override fun onDataChange(data: DataSnapshot) {
                val messages = arrayListOf<Any>()
                var addedMessageItemsCount = 0
                val childrenCount = data.childrenCount.toInt()

                if (page > FIRST_PAGE && childrenCount == 1) {
                    hasNext = false
                    isLoading = false
                    return
                }

                Observable.fromIterable(data.children)
                        .map { child ->
                            val value = child.getValue(Message::class.java)
                            messages.add(value!!)
                            addedMessageItemsCount++

                            return@map value
                        }
                        .scan(Message(), { prevItem, currentItem ->
                            val isLastItem = (addedMessageItemsCount == childrenCount)
                            if (isLastItem) {
                                return@scan currentItem
                            }

                            if (currentItem.createdAt - prevItem.createdAt >= ChatConstant.TIME_DISTANCE) {
                                messages.add(messages.size - 1, currentItem.createdAt)
                            }
                            return@scan currentItem
                        })
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally {
                            hasNext = childrenCount == ChatConstant.ITEM_MESSAGE_PER_PAGE
                            page++
                            isLoading = false
                        }
                        .subscribe({
                            //onNext: do nothing
                        }, { throwable ->
                            //onError:
                            throwable.printStackTrace()
                        }, {
                            //onComplete
                            if (messages.size >= 2) {
                                pivotMessageId = (messages[1] as Message).id

                                if (page > FIRST_PAGE) {
                                    messages.removeAt(messages.size - 1)
                                }
                            }

                            if (page == FIRST_PAGE) {
                                view?.showProgressBar(false)
                            } else {
                                val firstItemIsLoadingItem = listMessage.isNotEmpty() && listMessage.first() is LoadingItem
                                if (firstItemIsLoadingItem) {
                                    view?.removeLoadingItem(0)
                                }
                            }

                            val firstItemIsTime = listMessage.isNotEmpty() && listMessage.first() is Long
                            if (firstItemIsTime) {
                                val timeDistance = listMessage.first() as Long - (messages.last() as Message).createdAt
                                if (timeDistance < ChatConstant.TIME_DISTANCE) {
                                    view?.removeItem(0)
                                }
                            }

                            view?.addAll(messages, 0, page)
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

        //START add item time when time distance is greater than ChatConstant.TIME_DISTANCE
        if (listMessage.isEmpty()) {
            view?.add(0, message.createdAt, false)
        } else {
            if ((listMessage.last() as Message).isTypingMessage) {
                if (listMessage.size >= 2) {
                    //check the penultimate item
                    val isTimeDistanceWithLastItemExceed = listMessage[listMessage.size - 2] is Message && message.createdAt - (listMessage[listMessage.size - 2] as Message).createdAt > ChatConstant.TIME_DISTANCE
                    if (isTimeDistanceWithLastItemExceed) {
                        //insert item time to the before position of typing message
                        view?.add(listMessage.size - 1, message.createdAt, false)
                    }
                } else {
                    //list have only one typing message
                    view?.add(0, message.createdAt, false)
                }
            } else {
                //check the last item
                val isTimeDistanceWithLastItemExceed = listMessage.last() is Message && message.createdAt - (listMessage.last() as Message).createdAt > ChatConstant.TIME_DISTANCE
                if (isTimeDistanceWithLastItemExceed) {
                    view?.add(listMessage.size, message.createdAt, false)
                }
            }
        }
        //END add item time when time distance is greater than ChatConstant.TIME_DISTANCE

        addMessage(message, true)

        conversationRef?.child(ChatConstant.MESSAGES)?.child(message.id)?.setValue(message)
    }

    private fun addMessage(message: Message?, isClearEditText: Boolean) {
        val lastItemIsTypingMessage = listMessage.isNotEmpty()
                && listMessage.last() is Message
                && (listMessage.last() as Message).isTypingMessage

        if (lastItemIsTypingMessage) {
            view?.add(listMessage.size - 1, message, isClearEditText = isClearEditText)
        } else {
            view?.add(listMessage.size, message, isClearEditText = isClearEditText)
        }
    }
}
