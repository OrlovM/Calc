package com.calc.common

import android.annotation.SuppressLint
import android.os.Handler
import android.util.Log
import android.view.View
import com.calc.DateConverter
import com.calc.calculator.Calculator
import com.calc.calculator.IncorrectExpressionException
import com.calc.historyDB.HistoryManager
import com.calc.ui.CalcAdapter
import com.calc.ui.CalcSheetBehavior
import kotlinx.coroutines.*
import java.lang.Runnable
import java.util.*
import kotlin.collections.ArrayList


object CalcFacade {

    private var historyItemsDataSet = ArrayList<Expression>()
    var currentExpression = CurrentExpression("", "")
    private lateinit var adapter: CalcAdapter
//    private lateinit var calcSheetBehavior: CalcSheetBehavior<View>
    private var metrics = false

    var kolhoz = 0

    init {
//        testCourutine()
//        loadDatabaseInNewThread()
        historyItemsDataSet = HistoryManager.query()
        Log.i("CalcFacade", "Initialized ItemCount: ${historyItemsDataSet.size}")
    }

    fun initAdapter(adapter: CalcAdapter){
        this.adapter = adapter
    }

    fun testCourutine() {
        GlobalScope.launch {
            historyItemsDataSet = delay10000()
            adapter.notifyDataSetChanged()
        }
    }

    suspend fun delay10000(): ArrayList<Expression> {
        delay(5000)
        return HistoryManager.query()
    }

    private fun loadDatabaseInNewThread() {
        val handler = @SuppressLint("HandlerLeak")
        object: Handler() {}
        Thread(Runnable {
//            Thread.sleep(100)
            historyItemsDataSet = HistoryManager.query()
            Log.i("CalcFacade", "new Thread db loaded")
            handler.post(Runnable { adapter.notifyDataSetChanged() })
        }).start()

    }



//    fun initCalcSheet(calcSheetBehavior: CalcSheetBehavior<View>) {
//        this.calcSheetBehavior = calcSheetBehavior
//    }

    fun onItemClicked(position: Int, type: HistoryItem.Field) {
        currentExpression.expression = when (type)
        {
            HistoryItem.Field.Expression -> historyItemsDataSet[position].expression
            HistoryItem.Field.Value -> historyItemsDataSet[position].value
        }
        adapter.notifyDataSetChanged()

    }

    private fun dispatchCurrentExprChanged() {
//        adapter.notifyItemChanged(historyItemsDataSet.size)
        adapter.notifyDataSetChanged()
    }

    fun getItemCount(): Int {
        return historyItemsDataSet.size + kolhoz
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
            val value = (Calculator().calculate(currentExpression.expression, metrics))
            if (value != currentExpression.expression) {
                currentExpression.value = value
            }
        }catch (exception: IncorrectExpressionException) {
            currentExpression.value = ""
        }
        adapter.notifyItemChanged(historyItemsDataSet.size)
    }

    fun onCalculateClicked() {

        try {
            val value = (Calculator().calculate(currentExpression.expression, metrics))
            if (value != currentExpression.expression) {
                val converter = DateConverter()
                val itemToAdd = Expression(
                    currentExpression.expression,
                    value,
                    converter.convertDate(Date().time)
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

    fun clearCurrent() {
        currentExpression.expression = ""
        currentExpression.value = ""
        dispatchCurrentExprChanged()
    }

    fun clearHistory() {
        historyItemsDataSet.clear()
        HistoryManager.clearHistory()
        clearCurrent()
        adapter.notifyDataSetChanged()
    }

    fun setDegree(boolean: Boolean) {
        metrics = boolean
    }

}
