package com.example.ominext.plaidfork.ui.chat.adapter.viewholder

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.example.ominext.chatfirebase.R
import com.example.ominext.plaidfork.ui.chat.ChatConstant
import com.example.ominext.plaidfork.ui.chat.Message
import com.example.ominext.plaidfork.ui.chat.Utils

/**
 * Created by Ominext on 8/1/2017.
 */

class ItemChatLeftViewHolder(itemView: View, urlImage: String, accountType: Int,
                             private val adapterListener: AdapterListener?) : RecyclerView.ViewHolder(itemView) {

    @BindView(R.id.textview_chatleft_time)
    var tvTime: TextView? = null

    @BindView(R.id.textview_itemchatleft_message)
    var tvMessage: TextView? = null

    @BindView(R.id.imageview_typing)
    var imvTyping: ImageView? = null

    @BindView(R.id.imageview_image)
    var imvImage: ImageView? = null

    @BindView(R.id.cardview_image)
    var cardViewImage: CardView? = null

    @BindView(R.id.imageview_heart)
    var imvHeart: ImageView? = null

    private var isShowTime: Boolean = false

    init {
        ButterKnife.bind(this, itemView)
    }

    fun bindData(message: Message) {

        //        bindImage(message.isDisplayIcon());

        if (message.isTypingMessage) {
            bindDataMessageTyping()
        } else {
            bindDataMessage(message)
        }
    }

    private fun bindDataMessage(message: Message) {
        tvTime!!.text = Utils.getTimeAgoMessage(message.createdAt)
        Utils.hideView(imvTyping)

        when (message.messageType) {
            ChatConstant.MSG_TYPE_TEXT -> {
                tvMessage!!.text = message.message

                Utils.showView(tvMessage)
                Utils.hideView(imvHeart)
                Utils.hideView(cardViewImage)
                imvImage!!.setImageDrawable(null)
            }
            ChatConstant.MSG_TYPE_ICON_HEART -> {
                tvMessage!!.text = ""

                Utils.hideView(tvMessage)
                Utils.showView(imvHeart)
                Utils.hideView(cardViewImage)
                imvImage!!.setImageDrawable(null)
            }
            ChatConstant.MSG_TYPE_MEDIA -> {
                tvMessage!!.text = ""
                tvMessage!!.visibility = View.INVISIBLE
                Utils.hideView(imvHeart)

                Utils.showView(cardViewImage)
                Glide.with(itemView.context)
                        .load(message.message)
                        .thumbnail(0.5f)
                        .placeholder(R.drawable.dummy_image)
                        .error(R.drawable.dummy_image)
                        .into(imvImage)
            }
        }
    }

    private fun bindDataMessageTyping() {
        tvMessage!!.text = ""
        tvTime!!.text = ""

//        com.example.ominext.plaidfork.ui.chat.Utils.showView(imvTyping)
//        com.example.ominext.plaidfork.ui.chat.Utils.hideView(tvMessage)
//        com.example.ominext.plaidfork.ui.chat.Utils.hideView(imvHeart)
    }

    @OnClick(R.id.textview_itemchatleft_message, R.id.cardview_image, R.id.imageview_heart)
    internal fun onClickMessage() {
//        if (adapterListener != null && !adapterListener.isValidToShowTime(adapterPosition)) {
//            return
//        }
//        if (isShowTime) {
//            com.example.ominext.plaidfork.ui.chat.Utils.collapse(tvTime)
//            isShowTime = false
//        } else {
//            com.example.ominext.plaidfork.ui.chat.Utils.expand(tvTime)
//            isShowTime = true
//        }
    }

    interface AdapterListener {

        fun isValidToShowTime(position: Int): Boolean
    }
}