package com.example.ominext.chatfirebase

import android.app.Application
import com.example.ominext.chatfirebase.model.Status
import com.example.ominext.plaidfork.ui.chat.ChatConstant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


/**
 * Created by Ominext on 8/4/2017.
 */

class ChatApplication : Application() {

    var firebaseUser: FirebaseUser? = null

    lateinit var firebaseAuth: FirebaseAuth

    lateinit var db: DatabaseReference

    companion object {
        @JvmField var app: ChatApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser
        db = FirebaseDatabase.getInstance().reference

        if (firebaseUser != null) {
            db.child(ChatConstant.USERS).child(firebaseUser?.uid).child(ChatConstant.STATUS).setValue(Status.ONLINE.name)
            db.child(ChatConstant.USERS).child(firebaseUser?.uid).child(ChatConstant.LAST_ONLINE).setValue(null)
            db.child(ChatConstant.USERS).child(firebaseUser?.uid).child(ChatConstant.STATUS).onDisconnect().setValue(Status.OFFLINE.name)
            db.child(ChatConstant.USERS).child(firebaseUser?.uid).child(ChatConstant.LAST_ONLINE).onDisconnect().setValue(ServerValue.TIMESTAMP)

            val connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected")
            connectedRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val connected = snapshot.getValue(Boolean::class.java)!!
                    if (connected) {
                        println("connected")
                    } else {
                        println("not connected")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    System.err.println("Listener was cancelled")
                }
            })
        }
    }

    fun updateUser() {
        firebaseUser = firebaseAuth.currentUser
    }
}