package com.example.calc

import java.util.*

class RightParenthesis(private val inExpression: String, private val operatorStack: Stack<Char>, private val postfixExpression: ArrayList<String>): Token() {
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