package com.example.ominext.chatfirebase.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.ominext.chatfirebase.ChatApplication
import com.example.ominext.chatfirebase.constant.ChatConstant
import com.example.ominext.chatfirebase.model.User
import com.example.ominext.chatfirebase.util.Utils
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
    var isRegistered: Boolean = false

    fun addView(fragment: ChatListFragment, lifecycle: LifecycleRegistry) {
        view = fragment
        lifecycle.addObserver(this)
        listUser = ArrayList()
        userRef = ChatApplication.app?.db?.child(ChatConstant.USERS)?.ref
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (!isRegistered) {
            registerStatusListener()
            isRegistered = true
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        if (isRegistered) {
            unregisterStatusListener()
            isRegistered = false
        }
    }

    fun registerStatusListener() {
        childEventListener = userRef?.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                val user = p0?.getValue(User::class.java)
                updateUser(user)
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot?) {

            }

        })
    }

    fun unregisterStatusListener() {
        userRef?.removeEventListener(childEventListener)
        childEventListener = null
    }

    fun onClickItem(context: Context, position: Int) {
        if (!Utils.isNetworkAvailable(context)) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(view.context, p0.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChildren()) {
                    Observable.fromIterable(p0.children)
                            .map { child ->
                                child.getValue(User::class.java)
                            }
                            .filter { child ->
                                child?.uid != firebaseUser?.uid
                            }
                            .toList()
                            .subscribe { t1, _ ->
                                registerStatusListener()
                                view.insertUser(t1)
                                view.disableProgressbar()
                            }
                }
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