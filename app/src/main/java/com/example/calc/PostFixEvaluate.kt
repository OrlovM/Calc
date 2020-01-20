package com.example.calc

import java.util.*
import kotlin.collections.ArrayList

class PostFixEvaluate(private val postFixExpression: ArrayList<String>) {
    private val stack = Stack<Double>()
    private fun runBinary(operator: (Double, Double) -> Double) {
        val y = stack.pop()
        val x = stack.pop()
        val result = operator(x, y)
        stack.push(result)
    }

    fun evaluate(): String {
        stack.push(0.0)
        postFixExpression.forEach { current ->
            when (current) {
                "+" -> runBinary { x, y -> x + y }
                "-" -> runBinary { x, y -> x - y }
                "*" -> runBinary { x, y -> x * y }
                "/" -> runBinary { x, y -> x / y }
                "^" -> runBinary { x, y -> (Math.pow (x, y))}
                else -> stack.push(current.toDouble())
            }
        }
        return stack.peek().toString()
    }
}