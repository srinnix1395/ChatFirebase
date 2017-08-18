package com.example.ominext.chatfirebase.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.example.ominext.chatfirebase.R
import com.example.ominext.chatfirebase.model.Message
import com.example.ominext.chatfirebase.model.MessageType


/**
 * Created by anhtu on 8/13/2017.
 */
class MessageTextView : View {
    companion object {
        @JvmField
        val SIZE_LIKE = 100F
        @JvmField
        val SIZE_TYPING = 100F
    }

    var message: Message = Message()
        set(value) {
            field = value
            requestLayout()
        }

    var typingDrawable: Drawable? = null
        set(value) {
            field = value
            invalidate()
        }

    var typingSize: Float = SIZE_TYPING

    var likeDrawable: Drawable? = null
        set(value) {
            field = value
            invalidate()
        }

    var likeSize: Float = SIZE_LIKE

    var textColor: Int = Color.BLACK
        set(value) {
            field = value
            invalidate()
        }

    var textSize: Float = 14f
        set(value) {
            field = value
            calculateHeight()
            requestLayout()
        }

    var textMaxWidth: Float = 0f
        set(value) {
            field = value
            requestLayout()
        }

    private lateinit var textPaint: TextPaint
    private lateinit var fontMetric: Paint.FontMetrics
    private lateinit var staticLayout: StaticLayout

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.MessageTextView)
        typingDrawable = typedArray.getDrawable(R.styleable.MessageTextView_typing_src)
        textColor = typedArray.getColor(R.styleable.MessageTextView_text_color, Color.BLACK)
        textSize = typedArray.getDimension(R.styleable.MessageTextView_text_size, 14f)
        likeDrawable = typedArray.getDrawable(R.styleable.MessageTextView_like_src)
        textMaxWidth = typedArray.getDimension(R.styleable.MessageTextView_text_maxWidth, 0f)
        likeSize = typedArray.getDimension(R.styleable.MessageTextView_like_size, SIZE_LIKE)
        typingSize = typedArray.getDimension(R.styleable.MessageTextView_typing_size, SIZE_TYPING)
        typedArray.recycle()

        init()
    }

    private fun init() {
        textPaint = TextPaint()

        textPaint.isAntiAlias = true
        textPaint.textSize = textSize
        textPaint.color = textColor

        fontMetric = textPaint.fontMetrics

        message.message = "Quốc lộ 1 qua Cai Lậy, Tiền Giang là tuyến giao thông \"xương sống\", nối các tỉnh đồng bằng sông Cửu Long với TP HCM nhưng thường xuyên kẹt xe những ngày lễ, Tết. Tỉnh Tiền Giang đã đề nghị làm tuyến tránh qua đoạn đường này."
        calculateHeight()
    }

    private fun calculateHeight() {
        if (message.messageType == MessageType.TEXT.name && message.message != null) {
            val widthText = textPaint.measureText(message.message).toInt()

            staticLayout = StaticLayout(message.message, textPaint, widthText, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val width: Float
        val height: Float

        val modeWidth = MeasureSpec.getMode(widthMeasureSpec)
        val widthRequirement = MeasureSpec.getSize(widthMeasureSpec)
        val modeHeight = MeasureSpec.getMode(heightMeasureSpec)
        val heightRequirement = MeasureSpec.getSize(heightMeasureSpec)

        val pair = when (message.messageType) {
            MessageType.TEXT.name -> onMeasureText(modeWidth, widthRequirement, modeHeight, heightRequirement)
            MessageType.LIKE.name -> onMeasureLike(modeWidth, widthRequirement, modeHeight, heightRequirement)
            else -> onMeasureTyping(modeWidth, widthRequirement, modeHeight, heightRequirement)
        }

        width = pair.first
        height = pair.second

        setMeasuredDimension(width.toInt(), height.toInt())
    }

    private fun onMeasureText(modeWidth: Int, widthRequirement: Int, modeHeight: Int, heightRequirement: Int): Pair<Float, Float> {
        var width: Float
        var height: Float

        if (modeWidth == MeasureSpec.EXACTLY) {
            width = widthRequirement.toFloat()
        } else {
            width = staticLayout.width.toFloat() + paddingLeft + paddingRight

            if (modeWidth == MeasureSpec.AT_MOST) {
                // check if max width is not set, compare static layout with widthRequirement and
                // if max width is set, compare with min of widthRequirement and maxWidth
                val widthRequireWithMaxWidth: Float = if (textMaxWidth == 0f) {
                    widthRequirement.toFloat()
                } else {
                    Math.min(widthRequirement.toFloat(), textMaxWidth)
                }
                if (width > widthRequireWithMaxWidth) {
                    width = widthRequireWithMaxWidth
                    staticLayout = StaticLayout(message.message, textPaint, width.toInt() - paddingLeft - paddingRight, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)
                }
            }
        }

        if (modeHeight == MeasureSpec.EXACTLY) {
            height = heightRequirement.toFloat()
        } else {
            height = staticLayout.height.toFloat() + paddingTop + paddingBottom
            if (modeHeight == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightRequirement.toFloat())
            }
        }

        return Pair(width, height)
    }

    private fun onMeasureLike(modeWidth: Int, widthRequirement: Int, modeHeight: Int, heightRequirement: Int): Pair<Float, Float> {
        val width = when (modeWidth) {
            MeasureSpec.EXACTLY -> widthRequirement.toFloat()
            MeasureSpec.AT_MOST -> Math.min(widthRequirement.toFloat(), likeDrawable?.intrinsicWidth!!.toFloat())
            else -> {
                //MeasureSpec.UNSPECIFIED
                likeDrawable?.intrinsicWidth!!.toFloat()
            }
        }

        val height = when (modeHeight) {
            MeasureSpec.EXACTLY -> heightRequirement.toFloat()
            MeasureSpec.AT_MOST -> Math.min(heightRequirement.toFloat(), likeSize)
            else -> {
                //MeasureSpec.UNSPECIFIED
                likeSize
            }
        }

        return Pair(width, height)
    }

    private fun onMeasureTyping(modeWidth: Int, widthRequirement: Int, modeHeight: Int, heightRequirement: Int): Pair<Float, Float> {
        val width: Float = when (modeWidth) {
            MeasureSpec.EXACTLY -> widthRequirement.toFloat()
            MeasureSpec.AT_MOST -> Math.min(widthRequirement.toFloat(), (typingDrawable?.intrinsicWidth!! + paddingLeft + paddingRight).toFloat())
            else -> {
                //MeasureSpec.UNSPECIFIED
                (typingDrawable?.intrinsicWidth!! + paddingLeft + paddingRight).toFloat()
            }
        }

        val height = when (modeHeight) {
            MeasureSpec.EXACTLY -> heightRequirement.toFloat()
            MeasureSpec.AT_MOST -> Math.min(heightRequirement.toFloat(), typingSize)
            else -> {
                //MeasureSpec.UNSPECIFIED
                typingSize
            }
        }

        return Pair(width, height)
    }

    override fun draw(canvas: Canvas?) {
        if (message.messageType == MessageType.LIKE.name) {
            background = null
        }
        super.draw(canvas)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        when (message.messageType) {
            MessageType.TEXT.name -> {
                drawMessage(canvas)
            }
            MessageType.TYPING.name -> {
                drawMessageTyping(canvas)
            }
            MessageType.LIKE.name -> drawIconLike(canvas)
        }
    }

    private fun drawMessage(canvas: Canvas) {
        canvas.save()
        canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
        staticLayout.draw(canvas)
        canvas.restore()
    }

    private fun drawMessageTyping(canvas: Canvas) {
        val top = (height - typingDrawable?.intrinsicHeight!!) / 2
        typingDrawable?.setBounds(paddingLeft, top, typingDrawable?.intrinsicWidth!!, top + typingDrawable?.intrinsicHeight!!)
        typingDrawable?.draw(canvas)
    }

    private fun drawIconLike(canvas: Canvas) {
        if (likeDrawable != null) {
            val top = (height - likeDrawable?.intrinsicHeight!!) / 2
            likeDrawable?.setBounds(0, top, likeDrawable?.intrinsicWidth!!, top + likeDrawable?.intrinsicHeight!!)
            likeDrawable?.draw(canvas)
        }
    }
}