package com.calc.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.calc.R

class CalcItemDecorator(var context: Context): RecyclerView.ItemDecoration() {

    private val usualTopOffset = 20.toDp()
    private val dateTopOffset = 35.toDp()
    private val usualbottomOffset = 25.toDp()
    private val currentExrBottomOffset = 5.toDp()
    private val TAG = "CalcItemDecorator"
    val paint = Paint()
    val textPaint = Paint()


//    private fun Int.toDp(): Int = TypedValue.applyDimension(
//        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
//    ).toInt()

    private fun Int.toDp(): Int = Math.round(this.toFloat()*context.getResources().getDisplayMetrics().density).toInt()

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
            currentExpression -> if ((parent.getChildViewHolder(view) as? CalcAdapter.CurrentExpressionVH)?.expression?.text.toString() == "") {usualTopOffset} else {dateTopOffset}
            else -> usualTopOffset
        }
        val bottonOffset = when (viewType) {
            currentExpression -> currentExrBottomOffset
            else -> usualTopOffset
        }
        outRect.set(0,topOffset,0, bottonOffset)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {


        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        textPaint.color = parent.context.resources.getColor(R.color.dateTextColor)
        textPaint.textSize = 30.toDp()/2.5.toFloat()
        textPaint.isAntiAlias = true
        textPaint.isFakeBoldText = true
        paint.color = Color.LTGRAY

        for (i in 0 until parent.childCount) {

            val view = parent.getChildAt(i)
            val viewType = parent.adapter?.getItemViewType(parent.getChildAdapterPosition(view))

            val test = viewType == currentExpression && (parent.getChildViewHolder(view) as CalcAdapter.CurrentExpressionVH).expression.text.toString() != "" ?: false

            if (test || viewType == itemWithDate) {
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
                    left.toFloat() + 20.toDp().toFloat(),
                    bottomDraw + textPaint.textSize + 15.toDp().toFloat(),
                    textPaint
                )
            }
        }
    }


}