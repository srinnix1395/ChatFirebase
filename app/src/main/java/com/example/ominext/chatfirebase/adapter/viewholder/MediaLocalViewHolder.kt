package com.example.ominext.chatfirebase.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ominext.chatfirebase.R
import com.example.ominext.chatfirebase.model.MediaLocal
import com.example.ominext.chatfirebase.util.Utils

/**
 * Created by anhtu on 4/24/2017.
 */

class MediaLocalViewHolder(itemView: View,
                           listener: (position: Int) -> Unit) : RecyclerView.ViewHolder(itemView) {

    @BindView(R.id.imageview_image)
    lateinit var imvImage: ImageView

    @BindView(R.id.imageview_alpha)
    lateinit var imvAlpha: ImageView

    @BindView(R.id.textview_gif)
    lateinit var tvGif: TextView

    @BindView(R.id.textview_video)
    lateinit var tvVideo: TextView

    init {
        ButterKnife.bind(this, itemView)
        itemView.setOnClickListener { v ->
            listener(adapterPosition)
        }
    }

    fun bindData(media: MediaLocal?) {
        media?.let {
            if (media.isVideo) {
                Glide.with(itemView.context)
                        .load(media.urlThumbnail)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.dummy_image)
                                .error(R.drawable.dummy_image))
                        .into(imvImage)

                tvVideo.text = Utils.getDurationVideo(media.duration)

                Utils.showView(tvVideo)
                Utils.hideView(tvGif)
            } else {
                Utils.hideView(tvVideo)

                if (media.isGIF) {
                    Glide.with(itemView.context)
                            .load(media.path)
                            .apply(RequestOptions()
                                    .placeholder(R.drawable.dummy_image)
                                    .error(R.drawable.dummy_image))
                            .into(imvImage)
                    Utils.showView(tvGif)
                } else {
                    Glide.with(itemView.context)
                            .load(media.path)
                            .apply(RequestOptions()
                                    .placeholder(R.drawable.dummy_image)
                                    .error(R.drawable.dummy_image))
                            .into(imvImage)
                    Utils.hideView(tvGif)
                }
            }
        }

        bindSelected(media?.isSelected)
    }

    fun bindSelected(isSelected: Boolean?) {
        if (isSelected!!) {
            imvAlpha.setImageResource(R.drawable.background_item_image_picker)
        } else {
            imvAlpha.setImageDrawable(null)
        }
    }
}
