package com.example.calc

import java.util.*


class ShuntingYardAlgorithm {
    fun makePostFix(formula: ArrayList<FormulaPart>): ArrayList<FormulaPart> {
        val postFixExpression = ArrayList<FormulaPart>()
        val stack = Stack<FormulaPart.RpnPart>()
        formula.forEach { part ->
            when (part) {
                is FormulaPart.Value -> postFixExpression.add(part)
                is FormulaPart.RpnPart.Operator -> {
                    if (stack.empty()) {
                        stack.push(part)
                    }
                    else {
                        while (!stack.empty() && stack.peek().priority >= part.priority) {
                            postFixExpression.add(stack.pop())
                        }
                        stack.push(part)
                    }
                }
                is FormulaPart.RpnPart.LeftBracket -> stack.push(part)
                is FormulaPart.RightBracket -> {
                    while (!stack.empty() && stack.peek().priority > 0) {
                        postFixExpression.add(stack.pop())
                    }
                    try {
                        stack.pop()
                    } catch (e: EmptyStackException) {
                        throw IncorrectExpressionException("'(' missing")
                    }
                    if (!stack.empty() && stack.peek().priority == 5) {
                        postFixExpression.add(stack.pop())
                    }
                }
                is FormulaPart.Semicolon -> {
                    while (!stack.empty() && stack.peek().priority > 0) {
                        postFixExpression.add(stack.pop())
                    }
                }
            }
        }
        while (!stack.empty()) {
            postFixExpression.add(stack.pop())
        }

        return postFixExpression
    }
}