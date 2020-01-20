package com.example.calc

import java.util.*
import kotlin.collections.ArrayList

class Calculator(private val inExpression: String) {
    val postFixExpression = ArrayList<String>()
    val operatorStack = Stack<Char>()
    val operand = Operand(inExpression, postFixExpression)
    val operator = Operator(inExpression, operatorStack, postFixExpression)
    val leftParenthesis = LeftParenthesis(inExpression, operatorStack)
    val rightParenthesis = RightParenthesis(inExpression, operatorStack, postFixExpression)
    val tokenArray = arrayOf(operand, operator, leftParenthesis, rightParenthesis)

    fun calculate(): String {
        val postFix = ShuntingYardAlgorithm(inExpression, operatorStack, tokenArray).makePostFix(postFixExpression)
        return PostFixEvaluate(postFix).evaluate()
    }
}