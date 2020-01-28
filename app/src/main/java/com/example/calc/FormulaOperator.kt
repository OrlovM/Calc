package com.example.calc

import java.util.*
import kotlin.math.*

enum class FormulaOperator(val priority: Int) {
    PLUS(2),
    MINUS(2),
    DIV(3),
    MUL(3),
    EXP(4),
    SIN(0),
    COS(0),
    TAN(0),
    COT(0),
    SQR(0),
    ATAN(0),
    PROC(0);


    fun process(stack: LinkedList<Double>): Double {
        return when (this) {
            PLUS -> binary(stack) { x, y -> x + y }
            MINUS -> binary(stack) { x, y -> x - y }
            DIV -> binary(stack) { x, y -> x / y }
            MUL -> binary(stack) { x, y -> x * y }
            EXP -> binary(stack) { x, y -> Math.pow(x, y) }
            SIN -> unary(stack) { x -> sin(x) }
            COS -> unary(stack) { x -> cos(x) }
            TAN -> unary(stack) { x -> tan(x) }
            COT -> unary(stack) { x -> 1/tan(x) }
            SQR -> unary(stack) { x -> sqrt(x) }
            ATAN -> unary(stack) { x -> atan(x) }
            PROC -> unary(stack) { x -> sin(x) }

        }
    }

    private fun binary(stack: LinkedList<Double>, operation: (Double, Double) -> Double): Double {
        val y = stack.removeLast()
        val x = stack.removeLast()
        return operation.invoke(x, y)
    }

    private fun unary(stack: LinkedList<Double>, operation: (Double) -> Double): Double {
        val x = stack.removeLast()
        return operation.invoke(x)
    }
}