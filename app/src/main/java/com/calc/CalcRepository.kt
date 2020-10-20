package com.calc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.calc.common.Expression
import com.calc.historyDB.HistoryDAO

class CalcRepository(private val histDAO: HistoryDAO) {

    private lateinit var historyData: MutableLiveData<ArrayList<Expression>>

    fun getHistoryData(): LiveData<ArrayList<Expression>> {
        if (!this::historyData.isInitialized) {
            historyData = MutableLiveData()
            historyData.value = query()
        }
        return historyData
    }

    fun insert(expression: Expression) {
        histDAO.insert(expression)
        historyData.postValue(query())
    }

    private fun query(): ArrayList<Expression> {
        return histDAO.query()
    }

    fun clearHistory() {
        histDAO.clearHistory()
        historyData.postValue(query())
    }

}