package com.example.ominext.chatfirebase

import android.app.Application
import com.example.ominext.plaidfork.ui.chat.ChatConstant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Ominext on 8/4/2017.
 */

class ChatApplication : Application() {

    val firebaseUser: FirebaseUser? by lazy {
        firebaseAuth.currentUser
    }

    val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    val db: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference
    }

    companion object {
        @JvmField var app: ChatApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        if (firebaseUser != null) {
            db.child(ChatConstant.USERS).child(firebaseUser?.uid).child("status").setValue(Status.ONLINE.ordinal)
            db.child(ChatConstant.USERS).child(firebaseUser?.uid).child("status").onDisconnect().setValue(Status.OFFLINE.ordinal)
        }
    }


}