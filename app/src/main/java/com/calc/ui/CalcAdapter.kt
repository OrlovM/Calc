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
import com.calc.common.CalcFacade
import com.calc.common.HistoryItem
import com.example.calc.R

const val itemWithDate = 1
const val itemWithoutDate = 2
const val currentExpression = 3

class CalcAdapter(private var calcFacade: CalcFacade) :
    RecyclerView.Adapter<CalcAdapter.MainViewHolder>() {

    init {
        calcFacade.initAdapter(this)
    }

    abstract class MainViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder(viewGroup)

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class CalcViewHolder(linearLayout: LinearLayout) : MainViewHolder(linearLayout) {
        var expression: TextView = linearLayout.findViewById(R.id.textView)
        var value: TextView = linearLayout.findViewById(R.id.textView2)
        var date = "Date"
    }

    class CurrentExpressionVH(constraintLayout: ConstraintLayout) : MainViewHolder(constraintLayout) {
        var expression: EditText = constraintLayout.findViewById(R.id.editText)
        var value: TextView = constraintLayout.findViewById(R.id.textView3)
    }


    override fun onViewAttachedToWindow(holder: MainViewHolder) {
        if (holder is CalcViewHolder) {
            holder.expression.setOnClickListener {CalcFacade.onItemClicked(holder.adapterPosition, HistoryItem.Field.Expression)}
            holder.value.setOnClickListener {CalcFacade.onItemClicked(holder.adapterPosition, HistoryItem.Field.Value)}
        }
    }

    override fun onViewDetachedFromWindow(holder: MainViewHolder) {
        if (holder is CalcViewHolder) {
            holder.expression.setOnClickListener(null)
            holder.value.setOnClickListener(null)
        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MainViewHolder {


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

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        if (holder is CalcViewHolder) {
            val currentDataSetItem = calcFacade.getDataSetItem(position) as Expression
            holder.date = currentDataSetItem.calendar.toString()
            holder.expression.text = currentDataSetItem.expression
            holder.value.text = currentDataSetItem.value
            holder.expression.showSoftInputOnFocus = false
        } else if (holder is CurrentExpressionVH){
            val currentDataSetItem = calcFacade.getDataSetItem(position) as CurrentExpression
            holder.expression.setText(currentDataSetItem.expression)
            holder.value.text = currentDataSetItem.value
//            holder.expression.showSoftInputOnFocus = false
        }
        Log.i("CalcAdapter", " onBindViewHolder $position")

    }



    override fun getItemViewType(position: Int): Int {

        val currentDataSetItem = calcFacade.getDataSetItem(position)

        return if (currentDataSetItem is CurrentExpression) {
            currentExpression
        }else if (currentDataSetItem is Expression) {
            if (position == 0 || currentDataSetItem.calendar != (calcFacade.getDataSetItem(position - 1) as Expression).calendar) {
                itemWithDate
            } else itemWithoutDate
        }else itemWithoutDate

//        return if (position == itemCount - 1) {
//            currentExpression
//        } else if (position == 0 || expressionDataset[position].calendar != expressionDataset[position - 1].calendar) {
//            return itemWithDate
//        } else itemWithoutDate

    }






    override fun getItemCount() = calcFacade.getItemCount()
}