package com.example.calc

import java.util.*
import kotlin.math.*

enum class ExpressionOperator(val priority: Int) {
    PLUS(2),
    MINUS(2),
    DIV(3),
    MUL(3),
    EXP(4),
    SIN(5),
    COS(5),
    TAN(5),
    COT(5),
    SQRT(5),
    ROOT(5),
    ATAN(5),
    PROC(5);


    fun process(stack: LinkedList<Double>): Double {
        return when (this) {
            PLUS -> binary(stack) { x, y -> x + y }
            MINUS -> binary(stack) { x, y -> x - y }
            DIV -> binary(stack) { x, y -> x / y }
            MUL -> binary(stack) { x, y -> x * y }
            EXP -> binary(stack) { x, y -> Math.pow(x, y) }
            SIN -> unary(stack) { x -> sin(Math.toRadians(x)) }
            COS -> unary(stack) { x -> cos(Math.toRadians(x)) }
            TAN -> unary(stack) { x -> tan(Math.toRadians(x)) }
            COT -> unary(stack) { x -> 1.0/tan(Math.toRadians(x)) }
            SQRT -> unary(stack) { x -> sqrt(x) }
            ATAN -> unary(stack) { x -> atan(Math.toRadians(x)) }
            PROC -> unary(stack) { x -> sin(x) }
            ROOT -> binary(stack) { x,y -> Math.exp(Math.log(x)/y) }
        }
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