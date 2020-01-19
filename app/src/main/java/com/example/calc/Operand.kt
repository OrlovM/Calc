package com.example.calc

import java.util.*

class Operand(val inExpression: String, val operatorStack: Stack<Char>, val postfixExpression: ArrayList<String>): Symbol() {
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
}