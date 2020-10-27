package com.calc.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.calc.common.CurrentExpression
import com.calc.common.Expression
import com.calc.common.HistoryItem
import com.example.calc.R

const val itemWithDate = 1
const val itemWithoutDate = 2
const val currentExpression = 3

class CalcAdapter: RecyclerView.Adapter<CalcAdapter.MainViewHolder>() {

    var historyDataSet = ArrayList<Expression>()
    var expressionCurrent = CurrentExpression("", "")

    abstract class MainViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder(viewGroup)

    class CalcViewHolder(linearLayout: LinearLayout) : MainViewHolder(linearLayout) {
        var expression: TextView = linearLayout.findViewById(R.id.textView)
        var value: TextView = linearLayout.findViewById(R.id.textView2)
        var date = "Date"
    }

    class CurrentExpressionVH(var constraintLayout: ConstraintLayout) : MainViewHolder(constraintLayout) {
        var expression: EditText = constraintLayout.findViewById(R.id.editText)
        var value: TextView = constraintLayout.findViewById(R.id.textView3)
    }


    override fun onViewAttachedToWindow(holder: MainViewHolder) {
        if (holder is CalcViewHolder) {
//            holder.expression.setOnClickListener {CalcFacade.onItemClicked(holder.adapterPosition, HistoryItem.Field.Expression)}
//            holder.value.setOnClickListener {CalcFacade.onItemClicked(holder.adapterPosition, HistoryItem.Field.Value)}

        }
        Log.i("CalcAdapter", "onViewAttached")
    }

    override fun onViewDetachedFromWindow(holder: MainViewHolder) {
        if (holder is CalcViewHolder) {
//            holder.expression.setOnClickListener(null)
//            holder.value.setOnClickListener(null)
        }
        Log.i("CalcAdapter", "onViewDetached")
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MainViewHolder {

        Log.i("CalcAdapter", "onCreateVH")
        return if (viewType == currentExpression) {
            val constraintLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.current_expression_item, parent, false) as ConstraintLayout
            CurrentExpressionVH(constraintLayout)
        } else {
            val linearLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.linear_item, parent, false) as LinearLayout
            CalcViewHolder(linearLayout)
        }
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        if (holder is CalcViewHolder) {
            val currentDataSetItem = getDataSetItem(position) as Expression
            holder.date = currentDataSetItem.calendar.toString()
            holder.expression.text = currentDataSetItem.expression
            holder.value.text = currentDataSetItem.value
            holder.expression.showSoftInputOnFocus = false
        } else if (holder is CurrentExpressionVH){
            val currentDataSetItem = getDataSetItem(position) as CurrentExpression
            holder.expression.setText(currentDataSetItem.expression)
            holder.value.text = currentDataSetItem.value
            holder.expression.showSoftInputOnFocus = false
//            holder.constraintLayout.calcSheetBehavior = calcSheetBehavior
        }
        Log.i("CalcAdapter", " onBindViewHolder $position")

    }

    override fun getItemViewType(position: Int): Int {

        val currentDataSetItem = getDataSetItem(position)

        return if (currentDataSetItem is CurrentExpression) {
            currentExpression
        }else if (currentDataSetItem is Expression) {
            if (position == 0 || currentDataSetItem.calendar != (getDataSetItem(position - 1) as Expression).calendar) {
                itemWithDate
            } else itemWithoutDate
        }else itemWithoutDate

    }

    override fun getItemCount() = historyDataSet.size + currentIsEmpty()

    private fun getDataSetItem(position: Int): HistoryItem {
        return when (position) {
            in 0 until historyDataSet.size -> {
                historyDataSet[position]
            }
            historyDataSet.size -> {
                expressionCurrent
            }
            else -> throw IllegalArgumentException("Position is out of range")
        }
    }

    private fun currentIsEmpty(): Int = when (expressionCurrent.value == "") {
        true -> 0
        else -> 1
    }
}

