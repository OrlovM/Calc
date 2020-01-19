package com.example.calc

import java.util.*

class ShuntingYardAlgorythm {
    fun makePostFix(inExpression: String): ArrayList<String> {
        val postFixExpression = ArrayList<String>()
        val operatorStack = Stack<Char>()
        val operand = Operand(inExpression, operatorStack, postFixExpression)
        val operator = Operator(inExpression, operatorStack, postFixExpression)
        val leftParenthesis = LeftParenthesis(inExpression, operatorStack)
        val rightParenthesis = RightParenthesis(inExpression, operatorStack, postFixExpression)
        for (current in 0..inExpression.length-1) {
            when {
                operand.belongs(current) -> operand.operate(current)
                operator.belongs(current) -> operator.operate(current)
                leftParenthesis.belongs(current) -> leftParenthesis.operate(current)
                rightParenthesis.belongs(current) -> rightParenthesis.operate(current)
            }

        }
        while (!operatorStack.empty()) {
            postFixExpression.add(operatorStack.pop().toString())
        }
        return postFixExpression
    }
}