package com.example.calc

import java.util.*

class LeftParenthesis(private val inExpression: String, private val operatorStack: Stack<Char>): Token() {
    override fun belongs(current: Int): Boolean {
        return inExpression[current] == '('
    }

    override fun operate(current: Int) {
        if (inExpression[current-1] in '0'..'9' || inExpression[current-1] == ')'){
            operatorStack.push('*')
            operatorStack.push('(')
        }
        else {
            operatorStack.push('(')
        }
    }
}
