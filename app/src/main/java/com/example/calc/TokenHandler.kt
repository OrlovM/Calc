package com.example.calc

import java.util.*

abstract class TokenHandler {
    abstract fun belongs(current: Int): Boolean
    abstract fun operate(current: Int)
    fun handle(current: Int): Boolean{
        var handled = false
        if (belongs(current)) {
            operate(current)
            handled = true
        }
        return handled
    }

    abstract fun postFixBelongs(currentToken: String): Boolean
    abstract fun postFixOperate(currentToken: String, stack: Stack<Double>)
    fun postFixHandle(currentToken: String, stack: Stack<Double>): Boolean{
        var handled = false
        if (postFixBelongs(currentToken)) {
            postFixOperate(currentToken, stack)
            handled = true
        }
        return handled
    }
}








