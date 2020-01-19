package com.example.calc

import java.util.*

class Operator(val inExpression: String, val operatorStack: Stack<Char>, val postfixExpression: ArrayList<String>): Symbol() {
    private fun prior (x: Char): Int {      //Приоритет операторов
        var pr = 0
        when (x) {
            '(' -> pr = 1
            '+', '-' -> pr = 2
            '*', '/'  -> pr = 3
            '^' -> pr = 4
        }
        return pr
    }
    override fun belongs(current: Int): Boolean {
        val operator = (inExpression[current] == '+' || inExpression[current] =='-' || inExpression[current] =='*' || inExpression[current] =='^' || inExpression[current] =='/')
        return operator
    }

    override fun operate(current: Int) {
        while (!operatorStack.empty() && prior(inExpression[current]) <= prior(operatorStack.peek())) {
            postfixExpression.add(operatorStack.pop().toString())
        }
        operatorStack.push(inExpression[current])
    }
}