package com.example.calc

import java.util.*
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class Trigonometric(private val inExpression: String, private val operatorStack: Stack<String>,
                    private val postfixExpression: ArrayList<String>): TokenHandler() {


    override fun belongs(current: Int): Boolean {
        return (inExpression[current] in 'a'..'z')
    }

    override fun operate(current: Int) {
        if (!operatorStack.empty() && operatorStack.peek()[0] in 'a'..'z') {
            val temp = operatorStack.pop()
            val temp2 = inExpression[current].toString()
            operatorStack.push("$temp$temp2")
        }
        else {
            if (current > 0 && inExpression[current-1] in '0'..'9'){
                operatorStack.push("*")
            }
            operatorStack.push(inExpression[current].toString())
        }
    }

    override fun postFixBelongs(currentToken: String): Boolean {
        return currentToken == "sin" || currentToken == "cos" || currentToken ==  "tan" || currentToken == "atan"
    }

    override fun postFixOperate(currentToken: String, stack: Stack<Double>) {
        val x = stack.pop()
        when (currentToken) {
            "sin" -> stack.push(sin(Math.toRadians(x)))
            "cos" -> stack.push(cos(Math.toRadians(x)))
            "tan" -> stack.push(tan(Math.toRadians(x)))
            "atan" -> stack.push(atan(Math.toRadians(x)))

        }
    }
}