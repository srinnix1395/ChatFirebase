package com.example.ominext.chatfirebase.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.ominext.chatfirebase.R
import com.example.ominext.plaidfork.ui.chat.view.ChatFragment

/**
 * Created by Ominext on 8/2/2017.
 */
class DetailChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_chat)

        val frm = ChatFragment()
        frm.arguments = intent.extras

        supportFragmentManager.beginTransaction()
                .add(R.id.layout_container, frm)
                .commit()
    }
}