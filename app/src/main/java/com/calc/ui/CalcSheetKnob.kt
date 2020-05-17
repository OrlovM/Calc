package com.calc.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


class CalcSheetKnob @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null): View(context, attrs) {

    private val paint = Paint()
    private val rect = RectF()

    override fun onDraw(canvas: Canvas) {
        val left = (width / 5*2).toFloat()
        val top = (height / 3).toFloat()
        val right = (width / 5*3).toFloat()
        val bottom = top * 2
        rect.set(left,top,right,bottom)
        paint.color = Color.LTGRAY
        paint.isAntiAlias = true
        canvas.drawRoundRect(rect, top/2, top/2, paint)
    }
}