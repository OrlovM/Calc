package com.example.calc

import java.util.*

class RightParenthesis(val inExpression: String, val operatorStack: Stack<Char>, val postfixExpression: ArrayList<String>): Symbol() {
    override fun belongs(current: Int): Boolean {
        return inExpression[current] == ')'
    }

    override fun operate(current: Int) {
        while (operatorStack.peek() != '(') {
            postfixExpression.add(operatorStack.pop().toString())
        }
        operatorStack.pop()
    }
}