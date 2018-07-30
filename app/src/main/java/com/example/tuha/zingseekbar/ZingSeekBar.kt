package com.example.tuha.zingseekbar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

class ZingSeekBar : View {

    companion object {
        const val DEFAULT_COLOR_TRACK = Color.GRAY
        const val DEFAULT_COLOR_THUMB = Color.BLUE
        const val DEFAULT_COLOR_TIME = Color.WHITE
        const val DEFAULT_RADIUS_THUMB = 4F
        const val DEFAULT_RADIUS_MAGNIFIER = 4F
        const val DEFAULT_PADDING_HORIZONTAL_THUMB = 4F
        const val DEFAULT_PADDING_VERTICAL_THUMB = 2F
        const val DEFAULT_MARGIN_MAGNIFIER = 10F
        const val DEFAULT_PADDING_HORIZONTAL_MAGNIFIER = 6F
        const val DEFAULT_PADDING_VERTICAL_MAGNIFIER = 4F
        const val DEFAULT_TEXT_SIZE_TIME = 12F
        const val DEFAULT_TEXT_SIZE_TIME_MAGNIFIER = 16F
        const val DEFAULT_MAX = 300
        const val DEFAULT_TRACK_WIDTH = 2F
    }

    var colorTrack: Int = DEFAULT_COLOR_TRACK
    var trackWidth: Float = DEFAULT_TRACK_WIDTH

    var colorThumb: Int = DEFAULT_COLOR_THUMB
    var radiusThumb: Float = DEFAULT_RADIUS_THUMB
    var paddingHorizontalThumb: Float = DEFAULT_PADDING_HORIZONTAL_THUMB
    var paddingVerticalThumb: Float = DEFAULT_PADDING_VERTICAL_THUMB
    var colorTextThumb: Int = DEFAULT_COLOR_TIME
    var textSizeThumb: Float = DEFAULT_TEXT_SIZE_TIME

    var colorMagnifier: Int = DEFAULT_COLOR_THUMB
    var radiusMagnifier: Float = DEFAULT_RADIUS_MAGNIFIER
    var spacingMagnifier: Float = DEFAULT_MARGIN_MAGNIFIER
    var paddingHorizontalMagnifier: Float = DEFAULT_PADDING_HORIZONTAL_MAGNIFIER
    var paddingVerticalMagnifier: Float = DEFAULT_PADDING_VERTICAL_MAGNIFIER
    var colorTextMagnifier: Int = DEFAULT_COLOR_TIME
    var textSizeMagnifier: Float = DEFAULT_TEXT_SIZE_TIME_MAGNIFIER

    var max: Int = DEFAULT_MAX
    var progress: Int = 0

    private val paintTrack: Paint = Paint()
    private val paintThumb: Paint = Paint()
    private val paintMagnifier: Paint = Paint()
    private val paintTextThumb: TextPaint = TextPaint()
    private val paintTextMagnifier: TextPaint = TextPaint()

    private var textThumbHeight: Float = 0F
    private var textMagnifierHeight: Float = 0F

    private val thumbHeight: Float
        get() {
            return textThumbHeight + 2 * paddingVerticalThumb
        }
    private val magnifierHeight: Float
        get() {
            return textMagnifierHeight + 2 * paddingVerticalMagnifier
        }

    private var rectTrack = RectF()
    private var rectThumb = RectF()
    private var rectMagnifier = RectF()
    private var text = ""

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        init()
    }

    private fun init() {
        paintTrack.apply {
            isAntiAlias = true
            color = colorTrack
            style = Paint.Style.FILL
        }

        paintThumb.apply {
            isAntiAlias = true
            color = colorThumb
            style = Paint.Style.FILL
        }

        paintMagnifier.apply {
            isAntiAlias = true
            color = colorMagnifier
            style = Paint.Style.FILL
        }

        paintTextThumb.apply {
            isAntiAlias = true
            color = colorTextThumb
            textSize = textSizeThumb
        }

        paintTextMagnifier.apply {
            isAntiAlias = true
            color = colorTextMagnifier
            textSize = textSizeMagnifier
        }

        text = getText(max, progress)
        textThumbHeight = calculateTextTimeHeight(paintTextThumb)
        textMagnifierHeight = calculateTextTimeHeight(paintTextMagnifier)
    }

    @SuppressLint("SwitchIntDef")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightSize = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val desiredHeight = thumbHeight + magnifierHeight + spacingMagnifier
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

        calculateTrackRect()
        calculateThumbRect()
        calculateMagnifierRect()
    }

    override fun onDraw(canvas: Canvas) {
        //draw track
        canvas.drawRoundRect(rectTrack, 2F, 2F, paintTrack)

        //draw thumb
        canvas.drawRoundRect(rectThumb, radiusThumb, radiusThumb, paintThumb)
        val xTextThumb = rectThumb.left + paddingHorizontalThumb
        val yTextThumb = rectThumb.height() / 2 - (paintTextThumb.descent() + paintTextThumb.ascent()) / 2
        canvas.drawText(text, xTextThumb, yTextThumb, paintTextThumb)

        //draw magnifier
        canvas.drawRoundRect(rectMagnifier, radiusMagnifier, radiusMagnifier, paintThumb)
        val xTextMagnifier = rectMagnifier.left + paddingHorizontalMagnifier
        val yTextMagnifier = rectMagnifier.height() / 2 - (paintTextMagnifier.descent() + paintTextMagnifier.ascent()) / 2
        canvas.drawText(text, xTextMagnifier, yTextMagnifier, paintTextMagnifier)
    }

    private fun getText(progress: Int, max: Int): String {
        val hourProgress: Int = progress / 3600
        val minuteProgress: Int = progress / 60
        val secondProgress: Int = progress % 60

        val hourMax: Int = max / 3600
        val minuteMax: Int = max / 60
        val secondMax: Int = max % 60

        var stringBuilderProgress = StringBuilder()
        var stringBuilderMax = StringBuilder()
        if (hourProgress > 0 || hourMax > 0) {
            stringBuilderProgress = stringBuilderProgress
                    .append(hourProgress)
                    .append(":")
                    .append(String.format("%02d", minuteProgress))
                    .append(":")
                    .append(String.format("%02d", secondProgress))

            stringBuilderMax = stringBuilderMax
                    .append(hourMax)
                    .append(":")
                    .append(String.format("%02d", minuteMax))
                    .append(":")
                    .append(String.format("%02d", secondMax))
        } else {
            stringBuilderProgress = stringBuilderProgress
                    .append(minuteProgress)
                    .append(":")
                    .append(String.format("%02d", secondProgress))

            stringBuilderMax = stringBuilderMax
                    .append(minuteMax)
                    .append(":")
                    .append(String.format("%02d", secondMax))
        }

        return stringBuilderProgress.append("/").append(stringBuilderMax.toString()).toString()
    }

    private fun calculateTrackRect() {
        val top = height - (thumbHeight / 2) - (trackWidth / 2)
        val bottom = top + trackWidth
        rectTrack.set(0F, top, width.toFloat(), bottom)
    }

    private fun calculateThumbRect() {
        val top = height - thumbHeight
        val bottom = height
        //todo calculate thumb rect
    }

    private fun calculateMagnifierRect() {
        //todo calculate magnifier rect
    }

    private fun calculateTextTimeHeight(paint: TextPaint): Float {
        val rect = Rect()
        val textSample = "0123456789/"
        paint.getTextBounds(textSample, 0, textSample.length, rect)
        return rect.height().toFloat()
    }
}