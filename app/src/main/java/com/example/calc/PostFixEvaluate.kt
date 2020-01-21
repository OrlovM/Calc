package com.example.calc

import java.util.*
import kotlin.collections.ArrayList

class PostFixEvaluate(private val postFixExpression: ArrayList<String>, private val tokenHandlerArray: Array<TokenHandler>) {
    private val stack = Stack<Double>()

    fun evaluate(): String {
        stack.push(0.0)
        postFixExpression.forEach { currentToken ->
            for (i in 0 until tokenHandlerArray.size)
                if (tokenHandlerArray[i].postFixHandle(currentToken, stack)) {
                    break
                }
        }
        return stack.peek().toString()
    }
}