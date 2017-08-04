package com.example.ominext.chatfirebase.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.ominext.chatfirebase.R
import com.example.ominext.chatfirebase.adapter.payload.StatusMessagePayload
import com.example.ominext.chatfirebase.adapter.viewholder.LoadingViewHolder
import com.example.ominext.chatfirebase.model.LoadingItem
import com.example.ominext.chatfirebase.model.User
import com.example.ominext.plaidfork.ui.chat.Message
import com.example.ominext.plaidfork.ui.chat.adapter.viewholder.ItemChatLeftViewHolder
import com.example.ominext.plaidfork.ui.chat.adapter.viewholder.ItemChatRightViewHolder
import com.example.ominext.plaidfork.ui.chat.adapter.viewholder.ItemChatTimeViewHolder
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Ominext on 8/2/2017.
 */


class ChatAdapter(val listMessage: ArrayList<Any>,
                  val currentUser: FirebaseUser?,
                  val friendUser: User?,
                  val mRetryListener: (() -> Unit)? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemChatLeftViewHolder.AdapterListener {

    companion object {
        @JvmField val ITEM_LOADING = 0
        @JvmField val ITEM_LEFT = 1
        @JvmField val ITEM_RIGHT = 2
        @JvmField val ITEM_TIME = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        when (viewType) {
            ITEM_LEFT -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_chat_left, parent, false)
                return ItemChatLeftViewHolder(view, friendUser?.photo, this)
            }
            ITEM_RIGHT -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_chat_right, parent, false)
                return ItemChatRightViewHolder(view, this)
            }
            ITEM_TIME -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_time_chat, parent, false)
                return ItemChatTimeViewHolder(view)
            }
            ITEM_LOADING -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_loading, parent, false)
                return LoadingViewHolder(view, mRetryListener)
            }
            else -> {
                return null
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>?) {
        if (payloads!!.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }

        //        if (holder instanceof ItemChatLeftViewHolder && payloads.get(0) instanceof ImagePayload) {
        //            ((ItemChatLeftViewHolder) holder).bindImage(((ImagePayload) payloads.get(0)).isDisplayIcon);
        //            return;
        //        }

        if (holder is ItemChatRightViewHolder && payloads[0] is StatusMessagePayload) {
            holder.bindStatusMessage(listMessage[position] as Message)
            return
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)) {
            ITEM_LOADING -> {
                (holder as? LoadingViewHolder)?.bindData(listMessage[position] as LoadingItem)
            }
            ITEM_LEFT -> {
                (holder as? ItemChatLeftViewHolder)?.bindData(listMessage[position] as Message)
            }
            ITEM_RIGHT -> {
                (holder as? ItemChatRightViewHolder)?.bindData(listMessage[position] as Message)
            }
            ITEM_TIME -> {
                (holder as? ItemChatTimeViewHolder)?.bindData(listMessage[position] as Long)
            }
        }
    }

    override fun getItemCount(): Int {
        return listMessage.size
    }

    override fun getItemViewType(position: Int): Int {
        val any = listMessage[position]

        return when (any) {
            is LoadingItem -> ITEM_LOADING
            is Message -> if (any.idSender == currentUser?.uid) {
                ITEM_RIGHT
            } else {
                ITEM_LEFT
            }
            else -> ITEM_TIME
        }
    }

    override fun isValidToShowTime(position: Int): Boolean {
        if (position == 0) {
            return true
        }
        return listMessage[position - 1] !is Long
    }

    fun add(index: Int = listMessage.size, message: Message) {
        listMessage.add(index, message)
        notifyItemInserted(index)
    }

    fun addAll(messages: ArrayList<Message>, position: Int) {
        listMessage.addAll(position, messages)
        notifyItemRangeInserted(position, messages.count())
    }
}
