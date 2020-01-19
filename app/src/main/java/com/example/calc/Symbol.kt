package com.example.calc

abstract class Symbol {
    abstract fun belongs(current: Int): Boolean
    abstract fun operate(current: Int)
}








