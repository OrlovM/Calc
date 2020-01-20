package com.example.calc

import java.util.*

class ShuntingYardAlgorithm(private val inExpression: String, private val operatorStack: Stack<Char>, private val tokenArray: Array<Token>) {
    fun makePostFix(postFixExpression: ArrayList<String>): ArrayList<String> { //Захерачить в аргумент массив объектов?
        for (current in 0 until inExpression.length) {
            for (i in 0 until tokenArray.size)
                if (tokenArray[i].handle(current)) {
                    break
                }
        }
        while (!operatorStack.empty()) {
            postFixExpression.add(operatorStack.pop().toString())
        }
        return postFixExpression
    }
}