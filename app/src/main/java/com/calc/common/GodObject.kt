package com.calc.common

import android.util.Log
import android.view.View
import com.calc.calculator.Calculator
import com.calc.calculator.IncorrectExpressionException

import com.calc.historyDB.HistoryManager
import com.calc.ui.CalcAdapter
import com.calc.ui.CalcSheetBehavior
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList


object GodObject {

    var historyItemsDataSet = ArrayList<Expression>()
    var currentExpression = CurrentExpression("", "")
    private lateinit var adapter: CalcAdapter
    private lateinit var calcSheetBehavior: CalcSheetBehavior<View>

    init {
        historyItemsDataSet = HistoryManager.query()
        Log.i("GodObject", "Initialized ItemCount: ${historyItemsDataSet.size}")
    }

    fun initAdapter(adapter: CalcAdapter){
        this.adapter = adapter
    }

    fun initCalcSheet(calcSheetBehavior: CalcSheetBehavior<View>) {
        this.calcSheetBehavior = calcSheetBehavior
    }

    fun onItemClicked(string: String) {
        //TODO Change to position argument sending
        currentExpression.expression = string
        adapter.notifyDataSetChanged()

    }

    private fun dispatchCurrentExprChanged() {
//        adapter.notifyItemChanged(historyItemsDataSet.size)
        adapter.notifyDataSetChanged()
    }

    fun getItemCount(): Int {
        return historyItemsDataSet.size+1
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
        try {
            val value = (Calculator().calculate(currentExpression.expression))
            if (value != currentExpression.expression) {
                currentExpression.value = value
            }
        }catch (exception: IncorrectExpressionException) {
            currentExpression.value = ""
        }
        adapter.notifyDataSetChanged()
    }

    fun onCalculateClicked() {

        try {
            val value = (Calculator().calculate(currentExpression.expression))
            if (value != currentExpression.expression) {
                val itemToAdd = Expression(
                    currentExpression.expression,
                    value,
                    Date().time
                )
                addToDataSet(itemToAdd)
                currentExpression.expression = value
                currentExpression.value = ""
                adapter.notifyItemInserted(historyItemsDataSet.size - 1)
                adapter.notifyItemChanged(historyItemsDataSet.size)
            }
        } catch (exception: IncorrectExpressionException) {
            currentExpression.value = exception.reason
            adapter.notifyItemChanged(historyItemsDataSet.size)
        }
    }

    fun onCClicked() {
        currentExpression.expression = currentExpression.expression.dropLast(1)
        adapter.notifyDataSetChanged()
    }

    fun clear() {
        currentExpression.expression = ""
        currentExpression.value = ""
        dispatchCurrentExprChanged()
    }

    fun clearHistory() {
        HistoryManager.clearHistory()
    }

}