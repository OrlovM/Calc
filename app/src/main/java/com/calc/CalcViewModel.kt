package com.calc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.calc.calculator.Calculator
import com.calc.calculator.IncorrectExpressionException
import com.calc.common.CurrentExpression
import com.calc.common.Expression
import java.util.*
import kotlin.collections.ArrayList

class CalcViewModel(private val calcRepository: CalcRepository) :ViewModel() {

    private lateinit var currentExpressionData: MutableLiveData<CurrentExpression>
    private var currentExpression = CurrentExpression("", "")
    private var metrics = false

    fun getCurrentExpressionData(): LiveData<CurrentExpression> {
        if (!this::currentExpressionData.isInitialized) {
            currentExpressionData = MutableLiveData()
            currentExpressionData.value = currentExpression
        }
        return currentExpressionData
    }

    fun getHistoryData(): LiveData<ArrayList<Expression>> = calcRepository.getHistoryData()

    fun onButtonClicked(onButton: String) {
        currentExpression.expression = "${currentExpression.expression}$onButton"
        try {
            val value = (Calculator().calculate(
                currentExpression.expression,
                metrics
            ))
            if (value != currentExpression.expression) {
                currentExpression.value = value
            }
        }catch (exception: IncorrectExpressionException) {
            currentExpression.value = ""
        }
        currentExpressionData.postValue(currentExpression)
    }

    fun clearHistory() = calcRepository.clearHistory()

    fun onCClicked() {
        currentExpression.expression = currentExpression.expression.dropLast(1)
        currentExpressionData.postValue(currentExpression)
    }

    fun onCalculateClicked() {

        try {
            val value = (Calculator().calculate(
                currentExpression.expression,
                metrics
            ))
            if (value != currentExpression.expression) {
                val converter = DateConverter()
                val itemToAdd = Expression(
                    currentExpression.expression,
                    value,
                    converter.convertDate(Date().time)
                )
                calcRepository.insert(itemToAdd)
                currentExpression.expression = value
                currentExpression.value = ""
            }
        } catch (exception: IncorrectExpressionException) {
            currentExpression.value = exception.reason
        }
        currentExpressionData.postValue(currentExpression)
    }

    fun clearCurrent() {
        currentExpression.expression = ""
        currentExpression.value = ""
        currentExpressionData.postValue(currentExpression)
    }

    fun setDegree(boolean: Boolean) {
        metrics = boolean
    }

}