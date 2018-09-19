package com.example.tuha.zingseekbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import java.util.*

class SpectrumSeekBar : View {

    companion object {
        private const val COUNT_PARTITION = 60
        private const val DEFAULT_MAX = 300

        private const val TYPE_DRAW_TRACK = 0
        private const val TYPE_DRAW_THUMB = 1
        private const val TYPE_DRAW_MIX = 2
    }

    var max: Int = DEFAULT_MAX
        set(value) {
            if (value != field) {
                field = value
                invalidate()
            }
        }
    var progress: Int = 0
        set(value) {
            if (value != field) {
                field = value
                invalidate()
            }
        }

    var heightArray: Array<Int>? = null
        set(value) {
            field = value
            invalidate()
        }

    var widthPartition: Int = 0
    var spacingPartition: Int = 2

    private val paintTrack: Paint = Paint()
    private val paintThumb: Paint = Paint()
    private val tempRect = Rect()
    private val random = Random()
    private var maxHeightPartition: Int = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
//        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ZingSeekBar)
//        typedArray.recycle()

        init()
    }

    private fun init() {
        paintTrack.apply {
            isAntiAlias = true
            color = Color.GRAY
            style = Paint.Style.FILL
        }

        paintThumb.apply {
            isAntiAlias = true
            color = Color.parseColor("#ff8800")
            style = Paint.Style.FILL
        }

        heightArray = Array(COUNT_PARTITION) {
            40 + random.nextInt(100)
        }
        maxHeightPartition = heightArray!!.max()!!
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightSize = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val desiredHeight = (maxHeightPartition + 20).toFloat()

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> {
                heightSize
            }
            MeasureSpec.AT_MOST -> {
                Math.min(desiredHeight, heightSize)
            }
            else -> { //MeasureSpec.UNSPECIFIED
                desiredHeight
            }
        }

        setMeasuredDimension(widthMeasureSpec, height.toInt())
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            val totalSpacing = (COUNT_PARTITION - 1) * spacingPartition
            widthPartition = (width - totalSpacing) / COUNT_PARTITION
        }
    }

    override fun onDraw(canvas: Canvas) {
        var paintedWidth = 0
        val widthProgress = width * progress / max

        heightArray?.forEach { heightPartition ->
            val left = paintedWidth
            val top = height / 2 - heightPartition / 2
            val right = left + widthPartition
            val bottom = top + heightPartition
            tempRect.set(left, top, right, bottom)

            val drawType = when {
                paintedWidth == widthProgress -> TYPE_DRAW_TRACK
                paintedWidth > widthProgress -> TYPE_DRAW_TRACK
                paintedWidth < widthProgress && (paintedWidth + widthPartition) == widthProgress -> TYPE_DRAW_THUMB
                paintedWidth < widthProgress && (paintedWidth + widthPartition) > widthProgress -> TYPE_DRAW_MIX
                else -> TYPE_DRAW_THUMB
            }
            when (drawType) {
                TYPE_DRAW_TRACK -> {
                    canvas.drawRect(tempRect, paintTrack)
                }
                TYPE_DRAW_THUMB -> {
                    canvas.drawRect(tempRect, paintThumb)
                }
                TYPE_DRAW_MIX -> {
                    val widthThumb = widthProgress - paintedWidth
                    tempRect.right = tempRect.left + widthThumb
                    canvas.drawRect(tempRect, paintThumb)

                    val widthTrack = (paintedWidth + widthPartition) - widthProgress
                    tempRect.right = tempRect.left + widthPartition
                    tempRect.left = tempRect.right - widthTrack
                    canvas.drawRect(tempRect, paintTrack)
                }
            }
            paintedWidth += widthPartition + spacingPartition
        }
    }
}