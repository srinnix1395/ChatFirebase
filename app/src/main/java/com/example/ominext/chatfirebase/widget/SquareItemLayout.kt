package com.example.ominext.chatfirebase.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.example.ominext.chatfirebase.R

/**
 * Created by anhtu on 4/6/2017.
 */

class SquareItemLayout : FrameLayout {

    private var measureType: Int = 0

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SquareItemLayout)
        measureType = typedArray.getInteger(R.styleable.SquareItemLayout_measureType, TYPE_WIDTH)
        typedArray.recycle()
    }

    constructor(context: Context) : super(context) {}

    fun setMeasureType(measureType: Int) {
        this.measureType = measureType
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthSpec = widthMeasureSpec
        var heightSpec = heightMeasureSpec
        setMeasuredDimension(widthSpec, heightSpec)

        val childSize: Int
        if (measureType == TYPE_WIDTH) {
            childSize = measuredWidth
        } else {
            childSize = measuredHeight
        }

        widthSpec = View.MeasureSpec.makeMeasureSpec(childSize,
                View.MeasureSpec.EXACTLY)
        heightSpec = widthMeasureSpec

        super.onMeasure(widthSpec, heightSpec)
    }

    companion object {
        val TYPE_WIDTH = 1
        val TYPE_HEIGHT = 2
    }
}
