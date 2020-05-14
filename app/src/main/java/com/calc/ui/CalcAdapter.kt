package com.calc.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.calc.R

const val itemWithDate = 1
const val itemWithoutDate = 2
const val currentExpression = 3

class CalcAdapter(private var expressionDataset: ArrayList<Expression>) :
    RecyclerView.Adapter<CalcAdapter.CalcViewHolder>() {



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class CalcViewHolder(linearLayout: ConstraintLayout) : RecyclerView.ViewHolder(linearLayout){
        var textA: TextView = linearLayout.findViewById<TextView>(R.id.textView4)
        var textB: TextView = linearLayout.findViewById<TextView>(R.id.textView3)
        var date = "Date"
//        var date: TextView = linearLayout.findViewById<TextView>(R.id.textView3)
//        var heigth = linearLayout.offsetTopAndBottom()


    }




    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CalcViewHolder {
        // create a new view

        val linearLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.current_expression_item, parent, false) as ConstraintLayout

        // set the view's size, margins, paddings and layout parameters


        return CalcViewHolder(linearLayout)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: CalcViewHolder, position: Int) {

        holder.date = expressionDataset[position].calendar.toString()
        holder.textA.text = expressionDataset[position].expression
        holder.textB.text = expressionDataset[position].value
        holder.textA.showSoftInputOnFocus = false
    }

    override fun getItemViewType(position: Int): Int {

        return if (position == itemCount - 1) {
            currentExpression
        } else if (position == 0 || expressionDataset[position].calendar != expressionDataset[position - 1].calendar) {
            return itemWithDate
        } else itemWithoutDate

    }






    override fun getItemCount() = expressionDataset.size
}