package com.calc.ui

import android.app.ActionBar
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.calc.R

const val itemWithDate = 1
const val itemWithoutDate = 2
const val currentExpression = 3

class MyAdapter(private val expressionDataset: Array<Expression>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(var linearLayout: LinearLayout) : RecyclerView.ViewHolder(linearLayout){
        var textA: TextView = linearLayout.findViewById<TextView>(R.id.textView)
        var textB: TextView = linearLayout.findViewById<TextView>(R.id.textView2)
        var date = "Date"
//        var date: TextView = linearLayout.findViewById<TextView>(R.id.textView3)
//        var heigth = linearLayout.offsetTopAndBottom()


    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.MyViewHolder {
        // create a new view

        var linearLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.linear_item, parent, false) as LinearLayout

        // set the view's size, margins, paddings and layout parameters


        return MyViewHolder(linearLayout)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.date = expressionDataset[position].calendar.time.toString()
        holder.textA.text = expressionDataset[position].aa
        holder.textB.text = expressionDataset[position].bb
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