package com.example.ominext.chatfirebase.adapter.viewholder

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ominext.chatfirebase.R
import com.example.ominext.chatfirebase.model.Message
import com.example.ominext.chatfirebase.model.StatusMessage
import com.example.ominext.chatfirebase.model.TypeMessage
import com.example.ominext.chatfirebase.util.Utils
import com.example.ominext.chatfirebase.widget.setTimeAgo

/**
 * Created by Ominext on 8/1/2017.
 */


class ItemChatRightViewHolder(itemView: View, private val adapterListener: ItemChatLeftViewHolder.AdapterListener?) : RecyclerView.ViewHolder(itemView) {
    @BindView(R.id.textview_itemchatright_message)
    lateinit var tvMessage: TextView

    @BindView(R.id.textview_chatright_time)
    lateinit var tvTime: TextView

    @BindView(R.id.imageview_seen)
    lateinit var imvSeen: ImageView

    @BindView(R.id.imageview_heart)
    lateinit var imvHeart: ImageView

    @BindView(R.id.imageview_image)
    lateinit var imvImage: ImageView

    @BindView(R.id.cardview_image)
    lateinit var cardViewImage: CardView

    private var isShowTime: Boolean = false

    init {
        ButterKnife.bind(this, itemView)
    }

    fun bindData(message: Message) {
        tvTime.setTimeAgo(message.createdAt)

        when (message.messageType) {
            TypeMessage.TEXT.name -> {
                tvMessage.text = message.message

                Utils.showView(tvMessage)
                Utils.hideView(imvHeart)
                Utils.hideView(cardViewImage)
                imvImage.setImageDrawable(null)
            }
            TypeMessage.LIKE.name -> {
                tvMessage.text = ""

                tvMessage.visibility = View.INVISIBLE
                Utils.showView(imvHeart)
                Utils.hideView(cardViewImage)
                imvImage.setImageDrawable(null)
            }
            TypeMessage.MEDIA.name -> {
                tvMessage.text = ""
                tvMessage.visibility = View.INVISIBLE
                Utils.hideView(imvHeart)

                Utils.showView(cardViewImage)
                Glide.with(itemView.context)
                        .load(message.message)
                        .thumbnail(0.5f)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.dummy_image)
                                .error(R.drawable.dummy_image))
                        .into(imvImage)
            }
        }

        bindStatusMessage(message)
    }

    fun bindStatusMessage(message: Message) {
        when (message.status) {
            StatusMessage.PENDING.name -> {
                imvSeen.setImageResource(R.drawable.ic_circle)
            }
            StatusMessage.COMPLETE.name -> {
                imvSeen.setImageResource(R.drawable.ic_check_fill)
            }
//            ChatConstant.FRIEND_RECEIVED -> {
//                imvSeen.setImageResource(R.drawable.ic_check_fill)
//            }
//            ChatConstant.HANDLE_COMPLETE -> {
//                if (imvSeen.drawable != null) {
//                    imvSeen.setImageDrawable(null)
//                }
//            }
        }
    }

    @OnClick(R.id.textview_itemchatright_message, R.id.cardview_image, R.id.imageview_heart)
    fun onClickMessage() {
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
