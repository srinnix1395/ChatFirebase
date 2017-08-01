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


class ItemChatRightViewHolder(itemView: View, private val adapterListener: ItemChatLeftViewHolder.AdapterListener?) : RecyclerView.ViewHolder(itemView) {
    @BindView(R.id.textview_itemchatright_message)
    internal var tvMessage: TextView? = null

    @BindView(R.id.textview_chatright_time)
    internal var tvTime: TextView? = null

    @BindView(R.id.imageview_seen)
    internal var imvSeen: ImageView? = null

    @BindView(R.id.imageview_heart)
    internal var imvHeart: ImageView? = null

    @BindView(R.id.imageview_image)
    internal var imvImage: ImageView? = null

    @BindView(R.id.cardview_image)
    internal var cardViewImage: CardView? = null

    private var isShowTime: Boolean = false

    init {
        ButterKnife.bind(this, itemView)
    }

    fun bindData(message: Message) {
        tvTime?.text = Utils.getTimeAgoMessage(message.createdAt)

        when (message.messageType) {
            ChatConstant.MSG_TYPE_TEXT -> {
                tvMessage?.text = message.message

                Utils.showView(tvMessage)
                Utils.hideView(imvHeart)
                Utils.hideView(cardViewImage)
                imvImage?.setImageDrawable(null)
            }
            ChatConstant.MSG_TYPE_ICON_HEART -> {
                tvMessage?.text = ""

                tvMessage?.visibility = View.INVISIBLE
                Utils.showView(imvHeart)
                Utils.hideView(cardViewImage)
                imvImage?.setImageDrawable(null)
            }
            ChatConstant.MSG_TYPE_MEDIA -> {
                tvMessage?.text = ""
                tvMessage?.visibility = View.INVISIBLE
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

        bindStatusMessage(message.status)
    }

    fun bindStatusMessage(status: Int) {
        when (status) {
            ChatConstant.PENDING -> {
                imvSeen?.setImageResource(R.drawable.ic_circle)
            }
            ChatConstant.SERVER_RECEIVED -> {
                imvSeen?.setImageResource(R.drawable.ic_check_outline)
            }
            ChatConstant.FRIEND_RECEIVED -> {
                imvSeen?.setImageResource(R.drawable.ic_check_fill)
            }
            ChatConstant.HANDLE_COMPLETE -> {
                if (imvSeen?.drawable != null) {
                    imvSeen?.setImageDrawable(null)
                }
            }
        }
    }

    @OnClick(R.id.textview_itemchatright_message, R.id.cardview_image, R.id.imageview_heart)
    internal fun onClickMessage() {
//        if (adapterListener != null && !adapterListener.isValidToShowTime(adapterPosition)) {
//            return
//        }
//        if (isShowTime) {
//            Utils.collapse(tvTime)
//            isShowTime = false
//        } else {
//            Utils.expand(tvTime)
//            isShowTime = true
//
//        }
    }
}
