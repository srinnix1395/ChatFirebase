package com.example.ominext.chatfirebase

import android.app.Application
import com.example.ominext.chatfirebase.constant.ChatConstant
import com.example.ominext.chatfirebase.model.Status
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
            val data = HashMap<String, Any?>()
            data.put(ChatConstant.STATUS, Status.ONLINE.name)
            data.put(ChatConstant.LAST_ONLINE, null)
            db.child(ChatConstant.USERS).child(firebaseUser?.uid).updateChildren(data)

            val mapOffline = HashMap<String, Any?>()
            mapOffline.put(ChatConstant.STATUS, Status.OFFLINE.name)
            mapOffline.put(ChatConstant.LAST_ONLINE, ServerValue.TIMESTAMP)
            db.child(ChatConstant.USERS).child(firebaseUser?.uid).onDisconnect().updateChildren(mapOffline)

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