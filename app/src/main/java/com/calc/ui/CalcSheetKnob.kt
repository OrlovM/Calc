package com.calc.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes

class CalcSheetKnob @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null): View(context, attrs) {

    private val paint = Paint()


    override fun onDraw(canvas: Canvas) {
        paint.color = Color.LTGRAY
        val left = (width / 5*2).toFloat()
        val top = (height / 3).toFloat()
        val right = (width / 5*3).toFloat()
        val bottom = top * 2
        val rect = RectF(left, top, right, bottom)
        canvas.drawRoundRect(rect, top/2, top/2, paint)
    }
}