package com.example.ominext.chatfirebase.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.example.ominext.chatfirebase.R
import com.example.ominext.chatfirebase.model.Status
import com.example.ominext.chatfirebase.model.User
import com.example.ominext.chatfirebase.util.Utils

/**
 * Created by Ominext on 8/2/2017.
 */
class UserViewHolder(view: View,
                     val onClickItem: (position: Int) -> Unit) : RecyclerView.ViewHolder(view) {

    @BindView(R.id.imvImage)
    lateinit var imvImage: ImageView

    @BindView(R.id.tvName)
    lateinit var tvName: TextView

    @BindView(R.id.imvStatus)
    lateinit var imvStatus: ImageView

    @BindView(R.id.tvLastOnline)
    lateinit var tvLastOnline: TextView

    init {
        ButterKnife.bind(this, itemView)
        itemView.setOnClickListener {
            onClickItem(adapterPosition)
        }
    }

    fun bindData(user: User?) {
        Glide.with(itemView.context)
                .load(user?.photo)
                .placeholder(R.drawable.dummy_image)
                .error(R.drawable.user)
                .into(imvImage)
        tvName.text = user?.name

        bindStatus(user)
        bindLastOnline(user)
    }

    fun bindStatus(user: User?) {
        if (user?.status == Status.ONLINE.name) {
            imvStatus.setImageResource(R.drawable.ic_status_online)
        } else {
            imvStatus.setImageResource(R.drawable.ic_status_offline)
        }
    }

    fun bindLastOnline(user: User?) {
        if (user?.status == Status.ONLINE.name) {
            tvLastOnline.text = "Đang online"
        } else {
            tvLastOnline.text = Utils.getTimeAgoUser(itemView.context, user?.lastOnline)
        }
    }
}