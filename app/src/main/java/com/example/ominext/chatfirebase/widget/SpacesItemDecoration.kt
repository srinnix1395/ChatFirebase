package com.example.ominext.chatfirebase.widget

/*

Decorator which adds spacing around the tiles in Health Grid layout RecyclerView. Apply to Health RecyclerView with:

    SpacesItemDecoration decoration = new SpacesItemDecoration(16);
    mRecyclerView.addItemDecoration(decoration);

Feel free to add any value you wish for SpacesItemDecoration. That value determines the amount of spacing.

Source: http://blog.grafixartist.com/pinterest-masonry-layout-staggered-grid/

*/

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.ominext.chatfirebase.util.Utils

class SpacesItemDecoration(mContext: Context,
                           space: Int,
                           private val spanCount: Int,
                           private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {

    private val mSpace: Int = Utils.dpToPixel(mContext, space.toFloat())

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        if (position < 0) {
            return
        }

        val column = position % spanCount // item column

        if (includeEdge) {
            outRect.left = mSpace - column * mSpace / spanCount // mSpace - column * ((1f / spanCount) * mSpace)
            outRect.right = (column + 1) * mSpace / spanCount // (column + 1) * ((1f / spanCount) * mSpace)

            if (position < spanCount) { // top edge
                outRect.top = mSpace
            }
            outRect.bottom = mSpace // item bottom
        } else {
            outRect.left = column * mSpace / spanCount // column * ((1f / spanCount) * mSpace)
            outRect.right = mSpace - (column + 1) * mSpace / spanCount // mSpace - (column + 1) * ((1f /    spanCount) * mSpace)
            if (position >= spanCount) {
                outRect.top = mSpace // item top
            }
        }
    }
}
