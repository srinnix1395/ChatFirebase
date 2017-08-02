package com.example.ominext.chatfirebase.widget

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by Ominext on 8/2/2017.
 */


abstract class EndlessScrollDownListener(private val layoutManager: RecyclerView.LayoutManager) : RecyclerView.OnScrollListener() {
    private var isLoading: Boolean = false
    private var previousTotalItemCount = 0
    private val visibleThreshold: Int = 3

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy < 0) return

        val totalItemCount = layoutManager.itemCount
        var lastVisibleItemCount = 0

        if (layoutManager is LinearLayoutManager) {
            lastVisibleItemCount = layoutManager.findLastVisibleItemPosition()
        } else if (layoutManager is GridLayoutManager) {
            lastVisibleItemCount = layoutManager.findLastVisibleItemPosition()
        }

        //if it's still loading and current total item > previous total item -> disable loading
        if (isLoading && totalItemCount > previousTotalItemCount) {
            isLoading = false
            previousTotalItemCount = totalItemCount
        }

        //if it is not loading and users reach the threshold of loading more -> load more data
        val isReachThreshold = lastVisibleItemCount + visibleThreshold >= totalItemCount

        if (!isLoading && isReachThreshold) {
            onLoadMore()
            isLoading = true
        }
    }

    abstract fun onLoadMore()
}
