package com.example.ominext.chatfirebase.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.ominext.chatfirebase.R
import com.example.ominext.chatfirebase.adapter.payload.StatusMessagePayload
import com.example.ominext.chatfirebase.adapter.viewholder.LoadingViewHolder
import com.example.ominext.chatfirebase.model.LoadingItem
import com.example.ominext.chatfirebase.util.SharedPreUtils
import com.example.ominext.plaidfork.ui.chat.Message
import com.example.ominext.plaidfork.ui.chat.adapter.viewholder.ItemChatLeftViewHolder
import com.example.ominext.plaidfork.ui.chat.adapter.viewholder.ItemChatRightViewHolder
import com.example.ominext.plaidfork.ui.chat.adapter.viewholder.ItemChatTimeViewHolder
import java.util.*

/**
 * Created by Ominext on 8/2/2017.
 */


class ChatAdapter(context: Context,
                  val arrayList: ArrayList<Any>,
                  val urlImage: String?,
                  val mRetryListener: (() -> Unit)? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemChatLeftViewHolder.AdapterListener {

    companion object {
        @JvmField val ITEM_LOADING = 0
        @JvmField val ITEM_LEFT = 1
        @JvmField val ITEM_RIGHT = 2
        @JvmField val ITEM_TIME = 3
    }

    private val currentUserID: String? = SharedPreUtils.get(context).getUserId()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        when (viewType) {
            ITEM_LEFT -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_chat_left, parent, false)
                return ItemChatLeftViewHolder(view, urlImage, this)
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
            holder.bindStatusMessage((payloads[0] as StatusMessagePayload).status)
            return
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)) {
            ITEM_LOADING -> {
                (holder as? LoadingViewHolder)?.bindData(arrayList[position] as LoadingItem)
            }
            ITEM_LEFT -> {
                (holder as? ItemChatLeftViewHolder)?.bindData(arrayList[position] as Message)
            }
            ITEM_RIGHT -> {
                (holder as? ItemChatRightViewHolder)?.bindData(arrayList[position] as Message)
            }
            ITEM_TIME -> {
                (holder as? ItemChatTimeViewHolder)?.bindData(arrayList[position] as Long)
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun getItemViewType(position: Int): Int {
        val any = arrayList[position]

        return when (any) {
            is LoadingItem -> ITEM_LOADING
            is Message -> if (any.idSender == currentUserID) {
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
        return arrayList[position - 1] !is Long
    }
}
