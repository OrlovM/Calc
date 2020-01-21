package com.example.calc

import java.util.*

class ShuntingYardAlgorithm(private val inExpression: String, private val operatorStack: Stack<Char>,
                            private val tokenHandlerArray: Array<TokenHandler>) {
    fun makePostFix(postFixExpression: ArrayList<String>): ArrayList<String> {
        for (current in 0 until inExpression.length) {
            for (i in 0 until tokenHandlerArray.size)
                if (tokenHandlerArray[i].handle(current)) {
                    break
                }
        }
        while (!operatorStack.empty()) {
            postFixExpression.add(operatorStack.pop().toString())
        }
        return postFixExpression
    }
}