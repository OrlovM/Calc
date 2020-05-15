package com.calc.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.calc.GodObject
import com.example.calc.R

const val itemWithDate = 1
const val itemWithoutDate = 2
const val currentExpression = 3

class CalcAdapter(private var expressionDataset: ArrayList<HistoryItem>) :
    RecyclerView.Adapter<CalcAdapter.MainViewHolder>() {

    abstract class MainViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder(viewGroup) {

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class CalcViewHolder(linearLayout: LinearLayout) : MainViewHolder(linearLayout) {
        var textA: TextView = linearLayout.findViewById<TextView>(R.id.textView)
        var textB: TextView = linearLayout.findViewById<TextView>(R.id.textView2)
        var date = "Date"
//        var date: TextView = linearLayout.findViewById<TextView>(R.id.textView3)
//        var heigth = linearLayout.offsetTopAndBottom()
    }

    class CurrentExpressionVH(constraintLayout: ConstraintLayout) : MainViewHolder(constraintLayout) {
        var textA: TextView = constraintLayout.findViewById<TextView>(R.id.textView4)
        var textB: TextView = constraintLayout.findViewById<TextView>(R.id.textView3)
    }


//    override fun onViewAttachedToWindow(holder: MainViewHolder) {
//        holder.textA.setOnClickListener(){GodObject.onItemClicked(holder.textA.text.toString())}
//    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MainViewHolder {
//        // create a new view
//
//        val linearLayout = LayoutInflater.from(parent.context)
//            .inflate(R.layout.current_expression_item, parent, false) as ConstraintLayout
//
//        // set the view's size, margins, paddings and layout parameters
//
//
//        return CalcViewHolder(linearLayout)

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
            val currentDataSetItem = expressionDataset[position] as Expression
            holder.date = currentDataSetItem.calendar.toString()
            holder.textA.text = currentDataSetItem.expression
            holder.textB.text = currentDataSetItem.value
            holder.textA.showSoftInputOnFocus = false
        } else if (holder is CurrentExpressionVH){
            val currentDataSetItem = expressionDataset[position] as Expression
            holder.textA.text = currentDataSetItem.expression
            holder.textB.text = currentDataSetItem.value
            holder.textA.showSoftInputOnFocus = false
        }

    }

    override fun getItemViewType(position: Int): Int {

        val currentDataSetItem = expressionDataset[position]

        return if (currentDataSetItem is CurrentExpression) {
            currentExpression
        }else if (currentDataSetItem is Expression) {
            if (position == 0 || currentDataSetItem.calendar != (expressionDataset[position - 1] as Expression).calendar) {
                itemWithDate
            } else itemWithoutDate
        }else itemWithoutDate

//        return if (position == itemCount - 1) {
//            currentExpression
//        } else if (position == 0 || expressionDataset[position].calendar != expressionDataset[position - 1].calendar) {
//            return itemWithDate
//        } else itemWithoutDate

    }






    override fun getItemCount() = expressionDataset.size
}