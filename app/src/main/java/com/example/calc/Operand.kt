package com.example.calc

import java.lang.NumberFormatException
import java.util.*

class Operand(private val inExpression: String, private val postfixExpression: ArrayList<String>): TokenHandler() {
    var lastDigitIndex = 3
    override fun belongs(current: Int): Boolean {
        val digit = inExpression[current] in '0'..'9' || inExpression[current] == '.'
        val unarPrev = current == 0 || (inExpression[current - 1] != ')' && inExpression[current - 1] !in '0'..'9')
        val unarNext = current < inExpression.length - 1 && inExpression[current + 1] in '0'..'9'
        val unarMinus = inExpression[current] == '-' && unarPrev && unarNext
        return (digit || unarMinus)
    }

    override fun operate(current: Int) {
        if (current - lastDigitIndex == 1) {
            var asd: String = postfixExpression.last()
            val symbol = inExpression[current]
            asd = "$asd$symbol"
            postfixExpression.removeAt(postfixExpression.size - 1)
            postfixExpression.add(asd)
        } else {
            postfixExpression.add(inExpression[current].toString())              //Добавляем новый элемент списка
        }
        lastDigitIndex = current
    }

    override fun postFixBelongs(currentToken: String): Boolean =
        try {
            currentToken.toDouble()
            true
        }catch (e: NumberFormatException) {
            false
        }



    override fun postFixOperate(currentToken: String, stack: Stack<Double>) {
        stack.push(currentToken.toDouble())
    }
}