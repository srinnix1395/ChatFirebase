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
import com.example.ominext.chatfirebase.adapter.AdapterListener
import com.example.ominext.chatfirebase.model.Message
import com.example.ominext.chatfirebase.model.TypeMessage
import com.example.ominext.chatfirebase.util.Utils
import com.example.ominext.chatfirebase.widget.setTimeAgo

/**
 * Created by Ominext on 8/1/2017.
 */

class ItemChatLeftViewHolder(itemView: View,
                             urlImage: String?,
                             var positionItemShowTime: Int,
                             val adapterListener: AdapterListener) : RecyclerView.ViewHolder(itemView) {

    @BindView(R.id.textview_chatleft_time)
    lateinit var tvTime: TextView

    @BindView(R.id.textview_itemchatleft_message)
    lateinit var tvMessage: TextView

    @BindView(R.id.imageview_typing)
    lateinit var imvTyping: ImageView

    @BindView(R.id.imageview_image)
    lateinit var imvImage: ImageView

    @BindView(R.id.cardview_image)
    lateinit var cardViewImage: CardView

    @BindView(R.id.imageview_heart)
    lateinit var imvHeart: ImageView

    init {
        ButterKnife.bind(this, itemView)
    }

    fun bindData(message: Message) {

        //        bindImage(message.isDisplayIcon());

        if (message.isTypingMessage()) {
            bindDataMessageTyping()
        } else {
            bindDataMessage(message)
        }
    }

    private fun bindDataMessage(message: Message) {
        tvTime.setTimeAgo(message.createdAt)
        Utils.hideView(imvTyping)

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

                Utils.hideView(tvMessage)
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
    }

    private fun bindDataMessageTyping() {
        tvMessage.text = ""
        tvTime.text = ""

        Utils.showView(imvTyping)
        Utils.hideView(tvMessage)
        Utils.hideView(imvHeart)
    }

    @OnClick(R.id.textview_itemchatleft_message, R.id.cardview_image, R.id.imageview_heart)
    fun onClickMessage() {
        if (!adapterListener.isValidToShowTime(adapterPosition)) {
            return
        }
        if (positionItemShowTime == adapterPosition) {
            Utils.hideView(tvTime)
            positionItemShowTime = -1
        } else {
            Utils.showView(tvTime)
            positionItemShowTime = adapterPosition
        }
    }
}