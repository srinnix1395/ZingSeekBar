package com.example.tuha.zingseekbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

class ZingSeekBar : View {

    companion object {
        const val DEFAULT_COLOR_TRACK = Color.GRAY
        const val DEFAULT_COLOR_THUMB = Color.BLUE
        const val DEFAULT_COLOR_TIME = Color.WHITE
        const val DEFAULT_RADIUS_THUMB = 4F
        const val DEFAULT_MARGIN_MAGNIFIER = 10F
        const val DEFAULT_TEXT_SIZE_TIME = 12F
        const val DEFAULT_TEXT_SIZE_TIME_MAGNIFIER = 16F
        const val DEFAULT_MAX = 300
    }

    var colorTrack: Int = DEFAULT_COLOR_TRACK

    var colorThumb: Int = DEFAULT_COLOR_THUMB
    var radiusThumb: Float = DEFAULT_RADIUS_THUMB

    var colorTime: Int = DEFAULT_COLOR_TIME
    var textSizeTime: Float = DEFAULT_TEXT_SIZE_TIME

    var marginMagifier: Float = DEFAULT_MARGIN_MAGNIFIER
    var textSizeTimeMagnifier: Float = DEFAULT_TEXT_SIZE_TIME_MAGNIFIER

    var max: Int = DEFAULT_MAX
    var progress: Int = 0

    private val paintTrack: Paint = Paint()
    private val paintThumb: Paint = Paint()
    private val paintTime: TextPaint = TextPaint()
    private val paintTimeMagnifier: TextPaint = TextPaint()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){

        init()
    }

    private fun init() {
        paintTrack.apply {
            isAntiAlias = true
            color = colorTrack
        }

        paintThumb.apply {
            isAntiAlias = true
            color = colorThumb
        }

        paintTime.apply {
            isAntiAlias = true
            color = colorTime
            textSize = textSizeTime
        }

        paintTimeMagnifier.apply {
            isAntiAlias = true
            color = colorTime
            textSize = textSizeTimeMagnifier
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }


}