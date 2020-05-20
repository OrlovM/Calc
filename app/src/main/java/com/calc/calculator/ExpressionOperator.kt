package com.calc.calculator

import android.util.DisplayMetrics
import java.util.*
import kotlin.math.*

enum class ExpressionOperator(val priority: Int) {
    PLUS(2),
    MINUS(2),
    UNARYMINUS(5),
    DIV(3),
    MUL(3),
    EXP(4),
    SIN(6),
    COS(6),
    TAN(6),
    COT(6),
    SQRT(6),
    ROOT(6),
    ATAN(6),
    PROC(6);


    fun process(stack: LinkedList<Double>, metrics: Boolean = false): Double {
        return when (this) {
            PLUS -> binary(stack) { x, y -> x + y }
            MINUS -> binary(stack) { x, y -> x - y }
            DIV -> binary(stack) { x, y -> x / y }
            MUL -> binary(stack) { x, y -> x * y }
            EXP -> binary(stack) { x, y -> Math.pow(x, y) }
            SIN -> unary(stack) { x -> sin(toRadians(x, metrics)) }
            COS -> unary(stack) { x -> cos(toRadians(x, metrics)) }
            TAN -> unary(stack) { x -> tan(toRadians(x, metrics)) }
            COT -> unary(stack) { x -> 1.0/tan(toRadians(x, metrics)) }
            SQRT -> unary(stack) { x -> sqrt(x) }
            ATAN -> unary(stack) { x -> atan(toRadians(x, metrics)) }
            PROC -> unary(stack) { x -> sin(x) }
            ROOT -> binary(stack) { x, y -> Math.exp(Math.log(x)/y) }
            UNARYMINUS -> unary(stack) { x -> -x}
        }
    }

    private fun toRadians(x: Double, boolean: Boolean): Double {
        return if (boolean) {
            x
        } else Math.toRadians(x)
    }


    private fun binary(stack: LinkedList<Double>, operation: (Double, Double) -> Double): Double {
        val y: Double?
        val x = try {
            y = stack.removeLast()
            stack.removeLast()
        } catch (e: NoSuchElementException) {
            throw IncorrectExpressionException("Incorrect expression")
        }
        return operation.invoke(x, y)
    }

    private fun unary(stack: LinkedList<Double>, operation: (Double) -> Double): Double {
        val x = try {
            stack.removeLast()
        } catch (e: NoSuchElementException) {
            throw IncorrectExpressionException("Incorrect expression")
        }
        return operation.invoke(x)
    }
}