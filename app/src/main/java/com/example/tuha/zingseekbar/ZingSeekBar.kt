package com.example.tuha.zingseekbar

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

class ZingSeekBar : View {

    companion object {
        val DEFAULT_TRACK_COLOR = Color.parseColor("#909091")
        val DEFAULT_TRACK_WIDTH = DimensionUtils.dpToPx(2F)
        val DEFAULT_TRACK_RADIUS = DimensionUtils.dpToPx(2F)

        val DEFAULT_THUMB_COLOR = Color.parseColor("#8947ad")
        val DEFAULT_THUMB_TEXT_COLOR = Color.parseColor("#ffffff")
        val DEFAULT_THUMB_RADIUS = DimensionUtils.dpToPx(12F)
        val DEFAULT_THUMB_PADDING_HORIZONTAL = DimensionUtils.dpToPx(10F)
        val DEFAULT_PADDING_THUMB_VERTICAL = DimensionUtils.dpToPx(6F)
        val DEFAULT_THUMB_TEXT_SIZE = DimensionUtils.dpToPx(10F)

        val DEFAULT_MAGNIFIER_COLOR = Color.parseColor("#8947ad")
        val DEFAULT_MAGNIFIER_TEXT_COLOR = Color.parseColor("#ffffff")
        val DEFAULT_MAGNIFIER_RADIUS = DimensionUtils.dpToPx(18F)
        val DEFAULT_MAGNIFIER_PADDING_HORIZONTAL = DimensionUtils.dpToPx(8F)
        val DEFAULT_MAGNIFIER_PADDING_VERTICAL = DimensionUtils.dpToPx(8F)
        val DEFAULT_MAGNIFIER_TEXT_SIZE = DimensionUtils.dpToPx(14F)
        val DEFAULT_MAGNIFIER_SPACING = DimensionUtils.dpToPx(40F)

        const val DEFAULT_MAX = 300
    }

    var trackColor: Int = DEFAULT_TRACK_COLOR
    var trackWidth: Float = DEFAULT_TRACK_WIDTH

    var thumbColor: Int = DEFAULT_THUMB_COLOR
    var thumbRadius: Float = DEFAULT_THUMB_RADIUS
    var thumbPaddingHorizontal: Float = DEFAULT_THUMB_PADDING_HORIZONTAL
    var thumbPaddingVertical: Float = DEFAULT_PADDING_THUMB_VERTICAL
    var thumbTextColor: Int = DEFAULT_THUMB_TEXT_COLOR
    var thumbTextSize: Float = DEFAULT_THUMB_TEXT_SIZE

    var magnifierColor: Int = DEFAULT_MAGNIFIER_COLOR
    var magnifierRadius: Float = DEFAULT_MAGNIFIER_RADIUS
    var magnifierPaddingHorizontal: Float = DEFAULT_MAGNIFIER_PADDING_HORIZONTAL
    var magnifierPaddingVertical: Float = DEFAULT_MAGNIFIER_PADDING_VERTICAL
    var magnifierTextColor: Int = DEFAULT_MAGNIFIER_TEXT_COLOR
    var magnifierTextSize: Float = DEFAULT_MAGNIFIER_TEXT_SIZE
    var magnifierSpacing: Float = DEFAULT_MAGNIFIER_SPACING

    var max: Int = DEFAULT_MAX
        set(value) {
            if (value != field) {
                field = value
                changeText(0, field, true)
                calculateThumbRect()
                calculateMagnifierRect()
                invalidate()
            }
        }
    var progress: Int = 0
        set(value) {
            if (value != field) {
                field = value
                changeText(field, max, false)
                calculateThumbRect()
                calculateMagnifierRect()
                invalidate()
            }
        }

    private var textThumbWidth: Float = 0F
    private var textThumbHeight: Float = 0F
    private var textMagnifierWidth: Float = 0F
    private var textMagnifierHeight: Float = 0F
    private var textProgress = ""
    private var textMax = ""
    private var text = ""
    private var isShowMagnifier = true

    private val paintTrack: Paint = Paint()
    private val paintThumb: Paint = Paint()
    private val paintMagnifier: Paint = Paint()
    private val paintTextThumb: TextPaint = TextPaint()
    private val paintTextMagnifier: TextPaint = TextPaint()

    private val trackRect = RectF()
    private val thumbRect = RectF()
    private val magnifierRect = RectF()

    private val thumbWidth: Float
        get() {
            return textThumbWidth + 2 * thumbPaddingHorizontal
        }
    private val thumbHeight: Float
        get() {
            return textThumbHeight + 2 * thumbPaddingVertical
        }
    private val magnifierWidth: Float
        get() {
            return textMagnifierWidth + 2 * magnifierPaddingHorizontal
        }
    private val magnifierHeight: Float
        get() {
            return textMagnifierHeight + 2 * magnifierPaddingVertical
        }


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ZingSeekBar)

        trackColor = typedArray.getColor(R.styleable.ZingSeekBar_zing_track_color, trackColor)
        trackWidth = typedArray.getDimension(R.styleable.ZingSeekBar_zing_track_width, trackWidth)
        thumbColor = typedArray.getColor(R.styleable.ZingSeekBar_zing_thumb_color, thumbColor)
        thumbRadius = typedArray.getDimension(R.styleable.ZingSeekBar_zing_thumb_radius, thumbRadius)
        thumbPaddingHorizontal = typedArray.getDimension(R.styleable.ZingSeekBar_zing_thumb_padding_horizontal, thumbPaddingHorizontal)
        thumbPaddingVertical = typedArray.getDimension(R.styleable.ZingSeekBar_zing_thumb_padding_vertical, thumbPaddingVertical)
        thumbTextColor = typedArray.getColor(R.styleable.ZingSeekBar_zing_thumb_text_color, thumbTextColor)
        thumbTextSize = typedArray.getDimension(R.styleable.ZingSeekBar_zing_thumb_text_size, thumbTextSize)
        magnifierColor = typedArray.getColor(R.styleable.ZingSeekBar_zing_magnifier_color, magnifierColor)
        magnifierRadius = typedArray.getDimension(R.styleable.ZingSeekBar_zing_magnifier_radius, magnifierRadius)
        magnifierPaddingHorizontal = typedArray.getDimension(R.styleable.ZingSeekBar_zing_magnifier_padding_horizontal, magnifierPaddingHorizontal)
        magnifierPaddingVertical = typedArray.getDimension(R.styleable.ZingSeekBar_zing_magnifier_padding_vertical, magnifierPaddingVertical)
        magnifierTextColor = typedArray.getColor(R.styleable.ZingSeekBar_zing_magnifier_text_color, magnifierTextColor)
        magnifierTextSize = typedArray.getDimension(R.styleable.ZingSeekBar_zing_magnifier_text_size, magnifierTextSize)
        magnifierSpacing = typedArray.getDimension(R.styleable.ZingSeekBar_zing_magnifier_spacing, magnifierSpacing)

        typedArray.recycle()

        init()
    }

    private fun init() {
        paintTrack.apply {
            isAntiAlias = true
            color = trackColor
            style = Paint.Style.FILL
        }

        paintThumb.apply {
            isAntiAlias = true
            color = thumbColor
            style = Paint.Style.FILL
        }

        paintMagnifier.apply {
            isAntiAlias = true
            color = magnifierColor
            style = Paint.Style.FILL
        }

        paintTextThumb.apply {
            isAntiAlias = true
            color = thumbTextColor
            textSize = thumbTextSize
        }

        paintTextMagnifier.apply {
            isAntiAlias = true
            color = magnifierTextColor
            textSize = magnifierTextSize
        }

        changeText(progress, max, true)
        textThumbWidth = calculateTextWidth(paintTextThumb)
        textThumbHeight = calculateTextHeight(paintTextThumb)
        textMagnifierWidth = calculateTextWidth(paintTextMagnifier)
        textMagnifierHeight = calculateTextHeight(paintTextMagnifier)
    }

    @SuppressLint("SwitchIntDef")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightSize = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val desiredHeight = thumbHeight + magnifierHeight + magnifierSpacing
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
        canvas.drawRoundRect(trackRect, DEFAULT_TRACK_RADIUS, DEFAULT_TRACK_RADIUS, paintTrack)

        //draw thumb
        canvas.drawRoundRect(thumbRect, thumbRadius, thumbRadius, paintThumb)
        val xTextThumb = thumbRect.left + thumbPaddingHorizontal
        val yTextThumb = thumbRect.top + thumbRect.height() / 2 - (paintTextThumb.descent() + paintTextThumb.ascent()) / 2
        canvas.drawText(text, xTextThumb, yTextThumb, paintTextThumb)

        //draw magnifier
        if (isShowMagnifier) {
            canvas.drawRoundRect(magnifierRect, magnifierRadius, magnifierRadius, paintMagnifier)
            val xTextMagnifier = magnifierRect.left + magnifierPaddingHorizontal
            val yTextMagnifier = magnifierRect.top + magnifierRect.height() / 2 - (paintTextMagnifier.descent() + paintTextMagnifier.ascent()) / 2
            canvas.drawText(text, xTextMagnifier, yTextMagnifier, paintTextMagnifier)
        }
    }

    private fun changeText(progress: Int, max: Int, isChangeTextMax: Boolean) {
        val isShowHour = max >= 3600
        val format = if (isShowHour) {
            "%d:%02d:%02d"
        } else {
            "%d:%02d"
        }

        val hourProgress: Int = progress / 3600
        val minuteProgress: Int = progress / 60
        val secondProgress: Int = progress % 60
        textProgress = if (isShowHour) {
            String.format(format, hourProgress, minuteProgress, secondProgress)
        } else {
            String.format(format, minuteProgress, secondProgress)
        }

        if (isChangeTextMax) {
            val hourMax: Int = max / 3600
            val minuteMax: Int = max / 60
            val secondMax: Int = max % 60

            textMax = if (isShowHour) {
                String.format(format, hourMax, minuteMax, secondMax)
            } else {
                String.format(format, minuteMax, secondMax)
            }
        }

        text = buildString { append(textProgress).append(" / ").append(textMax) }
    }

    private fun calculateTrackRect() {
        val top = height - (thumbHeight / 2) - (trackWidth / 2)
        val bottom = top + trackWidth
        val left = 0F
        val right = width.toFloat()
        trackRect.set(left, top, right, bottom)
    }

    private fun calculateThumbRect() {
        val top = height - thumbHeight
        val bottom = height.toFloat()
        val left = ((width.toFloat() - thumbWidth) / max) * progress
        val right = left + thumbWidth
        thumbRect.set(left, top, right, bottom)
    }

    private fun calculateMagnifierRect() {
        val top = 0F
        val bottom = top + magnifierHeight
        var left: Float
        val right: Float
        if (progress <= max / 2) {
            left = if (((thumbRect.left + thumbRect.right) / 2 - magnifierWidth / 2) >= 0) {
                (thumbRect.left + thumbRect.right) / 2 - magnifierWidth / 2
            } else {
                0F
            }
            right = left + magnifierWidth
        } else {
            left = (thumbRect.left + thumbRect.right) / 2 - magnifierWidth / 2
            if (left + magnifierWidth > width) {
                right = width.toFloat()
                left = right - magnifierWidth
            } else {
                right = left + magnifierWidth
            }
        }
        magnifierRect.set(left, top, right, bottom)
    }

    private fun calculateTextWidth(paint: TextPaint): Float {
        val maxWidthOneDigit = (0..9).map { paint.measureText(it.toString()) }.max() ?: 0F
        val widthColon = paint.measureText(":")
        val widthSlash = paint.measureText("/")
        val widthSpace = paint.measureText(" ")
        var width = 0F
        text.forEach {
            when {
                it.isDigit() -> {
                    width += maxWidthOneDigit
                }
                it == ':' -> {
                    width += widthColon
                }
                it == ' ' -> {
                    width += widthSpace
                }
                it == '/' -> {
                    width += widthSlash
                }
            }
        }

        return width
    }

    private fun calculateTextHeight(paint: TextPaint): Float {
        val rect = Rect()
        val textSample = "0123456789/:"
        paint.getTextBounds(textSample, 0, textSample.length, rect)
        return rect.height().toFloat()
    }
}