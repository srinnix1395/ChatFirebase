package com.example.ominext.chatfirebase.adapter.viewholder

import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.ominext.chatfirebase.R
import com.example.ominext.chatfirebase.model.LoadingItem
import com.example.ominext.chatfirebase.model.STATE

/**
 * Created by Ominext on 8/2/2017.
 */


class LoadingViewHolder(itemView: View, val mRetryListener: (() -> Unit)?) : RecyclerView.ViewHolder(itemView) {
    @BindView(R.id.progressbar_itemloading)
    lateinit var progressBar: ProgressBar

    @BindView(R.id.imageview_retry)
    lateinit var imvRetry: ImageView

    init {
        ButterKnife.bind(this, itemView)
        progressBar.indeterminateDrawable.setColorFilter(
                ContextCompat.getColor(itemView.context, R.color.colorPrimary),
                PorterDuff.Mode.SRC_ATOP)
    }

    fun bindData(loadingItem: LoadingItem) {
        when (loadingItem.loadingState) {
            STATE.LOADING -> {
                imvRetry.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
            }
            STATE.ERROR -> {
                imvRetry.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE
            }
        }
    }

    @OnClick(R.id.imageview_retry)
    fun onClickRetry() {
        mRetryListener?.invoke()
    }
}
