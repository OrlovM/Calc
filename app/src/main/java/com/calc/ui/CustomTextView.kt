package com.calc.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

class CustomTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null): View(context, attrs) {


    var TAG = "CustomTextView"
    var text = "123456"
    var textColor = Color.BLACK
    var textSize = 60.0f
        set(value) {field = value
        invalidate()}
    var marginBottom = 30F
    var marginRight = 30F
    var textPaint = Paint().apply {
        color = textColor
        textSize = this@CustomTextView.textSize
        isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.i(TAG, "onMeasure $widthMeasureSpec $heightMeasureSpec")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        textPaint.textSize = textSize
        canvas.drawText(
            text,
            width - textPaint.measureText(text) - marginRight,
            height - marginBottom,
            textPaint
        )
        Log.i(TAG, "onDraw $width $height")
    }

}