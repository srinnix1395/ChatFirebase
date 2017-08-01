package com.example.ominext.plaidfork.ui.chat.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.ominext.plaidfork.R

/**
 * Created by Ominext on 8/1/2017.
 */


class ItemChatTimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @BindView(R.id.textview_time)
    var tvTime: TextView? = null

    init {
        ButterKnife.bind(this, itemView)
    }

    fun bindData(time: Long) {
        tvTime?.text = com.example.ominext.plaidfork.ui.chat.Utils.getTimeAgoMessage(time)
    }
}
