package com.example.ominext.chatfirebase.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.ominext.chatfirebase.R
import com.example.ominext.chatfirebase.util.Utils

/**
 * Created by Ominext on 8/1/2017.
 */


class ItemChatTimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @BindView(R.id.textview_time)
    lateinit var tvTime: TextView

    init {
        ButterKnife.bind(this, itemView)
    }

    fun bindData(time: Long) {
        tvTime.text = Utils.getTimeAgoMessage(time)
    }
}
