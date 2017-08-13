package com.example.ominext.chatfirebase.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.ominext.chatfirebase.model.Message
import com.example.ominext.chatfirebase.model.TypeMessage

/**
 * Created by anhtu on 8/13/2017.
 */
class MessageTextView : View {

    private val paint: Paint = Paint()

    var message: Message? = null
        set(value) {
            field = value
            invalidate()
        }

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        paint.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)


    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        when (message?.messageType) {
            TypeMessage.TEXT.name -> {
                drawBackground()
                drawMessage(canvas)
            }
            TypeMessage.TYPING.name -> {
                drawBackground()
                drawMessageTyping(canvas)
            }
            TypeMessage.LIKE.name -> drawIconLike(canvas)
        }
    }

    fun drawBackground() {

    }

    fun drawMessage(canvas: Canvas) {

        canvas.drawText(message?.message, paddingLeft.toFloat(), paddingTop.toFloat(), paint)
    }

    fun drawMessageTyping(canvas: Canvas) {

    }

    fun drawIconLike(canvas: Canvas) {

    }
}