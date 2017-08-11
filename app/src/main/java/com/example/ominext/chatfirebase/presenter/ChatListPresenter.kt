package com.example.ominext.chatfirebase.presenter

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleRegistry
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.ominext.chatfirebase.ChatApplication
import com.example.ominext.chatfirebase.constant.ChatConstant
import com.example.ominext.chatfirebase.model.User
import com.example.ominext.chatfirebase.util.Utils
import com.example.ominext.chatfirebase.util.toast
import com.example.ominext.chatfirebase.view.ChatListFragment
import com.example.ominext.chatfirebase.view.DetailChatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import io.reactivex.Observable

/**
 * Created by Ominext on 8/2/2017.
 */
class ChatListPresenter : LifecycleObserver {

    lateinit var view: ChatListFragment
    lateinit var listUser: ArrayList<User?>

    var firebaseUser: FirebaseUser? = ChatApplication.app?.firebaseUser
    var userRef: DatabaseReference? = null

    var childEventListener: ChildEventListener? = null
    var isLoadInitial: Boolean = false

    fun addView(fragment: ChatListFragment, lifecycle: LifecycleRegistry) {
        view = fragment
        lifecycle.addObserver(this)
        listUser = ArrayList()
        userRef = ChatApplication.app?.db?.child(ChatConstant.USERS)?.ref

        registerStatusListener()
    }

    fun registerStatusListener() {
        childEventListener = userRef?.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                if (isLoadInitial) {
                    val user = p0?.getValue(User::class.java)
                    println(user?.uid)
                    updateUser(user)
                }
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot?) {

            }

        })
    }

    fun onClickItem(context: Context, position: Int) {
        if (!Utils.isNetworkAvailable(context)) {
            toast("No internet connection")
            return
        }
        val intent = Intent(view.context, DetailChatActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(ChatConstant.USER, listUser[position])
        intent.putExtras(bundle)
        view.context.startActivity(intent)
    }

    fun getUsers() {
        userRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                view.disableProgressbar()
                toast(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChildren()) {
                    Observable.fromIterable(p0.children)
                            .map { child ->
                                child.getValue(User::class.java)
                            }
                            .filter { child ->
                                child.uid != firebaseUser?.uid
                            }
                            .toList()
                            .subscribe { t1, _ ->
                                view.insertUser(t1)
                                view.disableProgressbar()
                            }
                }
                isLoadInitial = true
            }
        })
    }


    private fun updateUser(userChanged: User?) {
        listUser.forEachIndexed { index, user ->
            if (userChanged?.uid == user?.uid) {
                userChanged?.let {
                    user?.status = userChanged.status
                    view.updateStatus(index, user?.status)
                }
                return
            }
        }
    }
}