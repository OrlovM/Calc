package com.calc

import android.util.Log
import com.calc.historyDB.HistoryManager
import com.calc.ui.CalcAdapter
import com.calc.ui.CurrentExpression
import com.calc.ui.Expression
import com.calc.ui.HistoryItem
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList

object GodObject {

    var historyItemsDataSet = ArrayList<Expression>()
    var currentExpression = CurrentExpression("","")
    private lateinit var adapter: CalcAdapter

    init {
        historyItemsDataSet = HistoryManager.query()
        Log.i("GodObject", "Initialized ItemCount: ${historyItemsDataSet.size}")
    }

    fun initAdapter(adapter: CalcAdapter){
        this.adapter = adapter
    }

    fun onItemClicked(string: String) {
        Log.i("ASD", "ТЫКНУТА $string")
    }

    private fun dispatchCurrentExprChanged() {
        adapter.notifyItemChanged(historyItemsDataSet.size)
    }

    fun getItemCount(): Int {
        return historyItemsDataSet.size
    }

    private fun addToDataSet(item: Expression) {
        historyItemsDataSet.add(item)
        HistoryManager.insert(item)
    }

    fun getDataSetItem(position: Int): HistoryItem {
        return when (position) {
            in 0 until historyItemsDataSet.size -> {
                historyItemsDataSet[position]
            }
            historyItemsDataSet.size -> {
                currentExpression
            }
            else -> throw IllegalArgumentException("Position is out of range")
        }
    }

    fun onButtonClicked(onButton: String) {
        currentExpression.expression = "${currentExpression.expression}$onButton"
        dispatchCurrentExprChanged()
    }

    fun onCalculateClicked() {
        val itemToAdd = Expression(currentExpression.expression, currentExpression.value, Date().time)
        historyItemsDataSet.add(itemToAdd)
        HistoryManager.insert(itemToAdd)
        currentExpression.expression = ""
        currentExpression.value = ""
        dispatchCurrentExprChanged()
    }


}