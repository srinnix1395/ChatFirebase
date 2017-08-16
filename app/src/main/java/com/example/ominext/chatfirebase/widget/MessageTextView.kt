package com.example.ominext.chatfirebase.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.example.ominext.chatfirebase.R
import com.example.ominext.chatfirebase.model.Message
import com.example.ominext.chatfirebase.model.TypeMessage



/**
 * Created by anhtu on 8/13/2017.
 */
class MessageTextView : View {

    private val paint: Paint = Paint()

    var message: Message = Message()
        set(value) {
            field = value
            invalidate()
        }

    var drawableTyping: Drawable? = null
    var drawableLike: Drawable? = null
    var textColor: Int = Color.BLACK
    var textSize: Float = 14f
    private var rectText: Rect = Rect()

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.MessageTextView)
        drawableTyping = typedArray.getDrawable(R.styleable.MessageTextView_typing_src)
        textColor = typedArray.getColor(R.styleable.MessageTextView_text_color, Color.BLACK)
        textSize = typedArray.getDimension(R.styleable.MessageTextView_text_size, 14f)
        drawableTyping = typedArray.getDrawable(R.styleable.MessageTextView_like_src)
        typedArray.recycle()

        paint.isAntiAlias = true
        paint.textSize = textSize
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        paint.getTextBounds(message.message ?: "hello", 0, message.message?.length ?: 5, rectText)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        when (message.messageType) {
            TypeMessage.TEXT.name -> {
                drawMessage(canvas)
            }
            TypeMessage.TYPING.name -> {
                drawMessageTyping(canvas)
            }
            TypeMessage.LIKE.name -> drawIconLike(canvas)
        }
    }

    fun drawMessage(canvas: Canvas) {
        paint.color = textColor
        val yPos = (canvas.height / 2 - (paint.descent() + paint.ascent()) / 2)

        canvas.drawText(message.message ?: "hello", paddingLeft.toFloat(), yPos, paint)
    }

    fun drawMessageTyping(canvas: Canvas) {

    }

    fun drawIconLike(canvas: Canvas) {

    }
}