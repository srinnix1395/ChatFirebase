package com.example.ominext.plaidfork.ui.chat.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ominext.chatfirebase.R

/**
 * Created by Ominext on 8/1/2017.
 */
class ChatFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_chat, container, false)
    }
}