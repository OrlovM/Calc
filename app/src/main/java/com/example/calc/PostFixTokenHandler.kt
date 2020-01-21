package com.example.calc

import java.util.*

interface PostFixTokenHandler {
    fun postFixBelongs(currentToken: String): Boolean
    fun postFixOperate(currentToken: String, stack: Stack<Double>)
    fun postFixHandle(currentToken: String, stack: Stack<Double>): Boolean{
        var handled = false
        if (postFixBelongs(currentToken)) {
            postFixOperate(currentToken, stack)
            handled = true
        }
        return handled
    }
}