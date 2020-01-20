package com.example.calc

abstract class Token {
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
}








