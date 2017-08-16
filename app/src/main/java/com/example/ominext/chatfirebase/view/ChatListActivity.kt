package com.example.ominext.chatfirebase.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.ominext.chatfirebase.ChatApplication
import com.example.ominext.chatfirebase.R

/**
 * Created by Ominext on 8/1/2017.
 */
class ChatListActivity : AppCompatActivity() {
//    @BindView(R.id.toolbar_main)
//    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a)
//        ButterKnife.bind(this)
//
//        toolbar.setTitleTextColor(Color.WHITE)
//        toolbar.title = "Fire chat"
//        toolbar.inflateMenu(R.menu.menu_main)
//        toolbar.setOnMenuItemClickListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.mi_sign_out -> signOut()
//            }
//            return@setOnMenuItemClickListener true
//        }
    }

    private fun signOut() {
        ChatApplication.app?.firebaseAuth?.signOut()

        val intent: Intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

        finish()
    }
}