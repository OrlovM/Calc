package com.example.calc

import java.util.*
import kotlin.collections.ArrayList

class Calculator(private val inExpression: String) {
    private val postFixExpression = ArrayList<String>()
    private val operatorStack = Stack<Char>()
    private val operand = Operand(inExpression, postFixExpression)
    private val operator = Operator(inExpression, operatorStack, postFixExpression)
    private val leftParenthesis = LeftParenthesis(inExpression, operatorStack)
    private val rightParenthesis = RightParenthesis(inExpression, operatorStack, postFixExpression)
    private val tokenArray = arrayOf(operand, operator, leftParenthesis, rightParenthesis)

    fun calculate(): String {
        val postFix = ShuntingYardAlgorithm(inExpression, operatorStack, tokenArray).makePostFix(postFixExpression)
        return PostFixEvaluate(postFix, tokenArray).evaluate()
    }
}