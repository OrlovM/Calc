package com.example.calc

import java.util.*

class Operator(private val inExpression: String, private val operatorStack: Stack<String>,
               private val postfixExpression: ArrayList<String>): TokenHandler() {
    private fun prior (x: String): Int {      //Приоритет операторов
        var pr = 0
        when (x) {
            "(" -> pr = 1
            "+", "-" -> pr = 2
            "*", "/"  -> pr = 3
            "^" -> pr = 4
        }
        return pr
    }
    override fun belongs(current: Int): Boolean {
        return (inExpression[current] == '+' || inExpression[current] =='-' || inExpression[current] =='*' || inExpression[current] =='^' || inExpression[current] =='/')
    }

    override fun operate(current: Int) {
        while (!operatorStack.empty() && prior(inExpression[current].toString()) <= prior(operatorStack.peek())) {
            postfixExpression.add(operatorStack.pop().toString())
        }
        operatorStack.push(inExpression[current].toString())
    }

    override fun postFixBelongs(currentToken: String): Boolean {
        return currentToken == "+" || currentToken == "-" || currentToken == "*" || currentToken == "/" || currentToken =="^"
    }

    override fun postFixOperate(currentToken: String, stack: Stack<Double>) {
        val y = stack.pop()
        val x = stack.pop()
        when (currentToken) {
            "+" -> stack.push(x + y)
            "-" -> stack.push(x - y)
            "*" -> stack.push(x * y)
            "/" -> stack.push(x / y)
            "^" -> stack.push(Math.pow(x, y))
        }
    }
}