package com.calc.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.calc.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CalcItemDecorator(var context: Context): RecyclerView.ItemDecoration() {

    private val usualTopOffset = 30.toDp()
    private val dateTopOffset = 50.toDp()
    private val bottomOffset = 30.toDp()
    private val TAG = "CalcItemDecorator"


    private fun Int.toDp(): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    ).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val viewType: Int = parent.adapter?.getItemViewType(position) ?: itemWithoutDate


        val topOffset = when (viewType) {
            itemWithDate -> dateTopOffset
            currentExpression -> dateTopOffset
            else -> usualTopOffset
        }

        outRect.set(0,topOffset,0,bottomOffset)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {


        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val paint = Paint()
        val textPaint = Paint()
        textPaint.color = parent.context.resources.getColor(R.color.dateTextColor)
        textPaint.textSize = 30.toDp()/2.5.toFloat()
        textPaint.isAntiAlias = true
        textPaint.isFakeBoldText = true
        paint.color = Color.LTGRAY

        for (i in 0 until parent.childCount) {

            val view = parent.getChildAt(i)
            val viewType = parent.adapter?.getItemViewType(parent.getChildAdapterPosition(view))
            if (viewType == currentExpression || viewType == itemWithDate) {
                val bottomDraw = view.top - dateTopOffset
                val topDraw = bottomDraw - 1.toDp()
                var text =
                    (parent.getChildViewHolder(view) as? CalcAdapter.CalcViewHolder)?.date ?: "Текущее выражение"

                c.drawRect(
                    left.toFloat(),
                    topDraw.toFloat(),
                    right.toFloat(),
                    bottomDraw.toFloat(),
                    paint
                )
                c.drawText(
                    text,
                    left.toFloat() + 30.0f,
                    bottomDraw + textPaint.textSize + 30.0f,
                    textPaint
                )
            }
        }
    }


}