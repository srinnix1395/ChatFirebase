package com.example.ominext.chatfirebase.presenter

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.ominext.chatfirebase.model.User
import com.example.ominext.chatfirebase.view.ChatListFragment
import com.example.ominext.chatfirebase.view.DetailChatActivity
import com.example.ominext.plaidfork.ui.chat.ChatConstant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

/**
 * Created by Ominext on 8/2/2017.
 */
class ChatListPresenter {

    lateinit var db: DatabaseReference
    lateinit var view: ChatListFragment
    lateinit var listUser: ArrayList<User>
    var firebaseUser: FirebaseUser? = null

    var offset: Double = 0.0

    fun addView(fragment: ChatListFragment) {
        view = fragment
        db = FirebaseDatabase.getInstance().reference
        listUser = ArrayList()
        firebaseUser = FirebaseAuth.getInstance().currentUser
    }

    fun onClickItem(position: Int) {
        val intent = Intent(view.context, DetailChatActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(ChatConstant.USER, listUser[position])
        intent.putExtras(bundle)
        view.context.startActivity(intent)
    }

    fun getUsers() {
        db.child(ChatConstant.USERS).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                view.disableProgressbar()
                Toast.makeText(view.context, p0.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChildren()) {
                    val users = arrayListOf<User>()
                    p0.children.forEach { snapshot ->
                        val user = snapshot.getValue(User::class.java)
                        if (user?.uid != firebaseUser?.uid) {
                            users.add(user!!)
                        }
                    }
                    view.insertUser(users)
                    view.disableProgressbar()
                }
            }
        })
    }


}