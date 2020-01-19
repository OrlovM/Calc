package com.example.calc

import java.util.*

class LeftParenthesis(val inExpression: String, val operatorStack: Stack<Char>): Symbol() {
    override fun belongs(current: Int): Boolean {
        return inExpression[current] == '('
    }

    override fun operate(current: Int) {
        operatorStack.push('(')
    }
}
