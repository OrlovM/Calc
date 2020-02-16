package com.example.calc

import java.util.*


class ShuntingYardAlgorithm {
    fun makePostFix(expression: List<ExpressionPart>): List<ExpressionPart.RpnPart> {
        val postFixExpression = ArrayList<ExpressionPart>()
        val stack = Stack<ExpressionPart>()
        expression.forEach { part ->
            when (part) {
                is ExpressionPart.RpnPart.Value -> postFixExpression.add(part)
                is ExpressionPart.RpnPart.Operator -> {
                    while (!stack.empty() && stack.peek().priority() >= part.priority()) {
                        postFixExpression.add(stack.pop())
                    }
                    stack.push(part)
                }
                is ExpressionPart.LeftBracket -> stack.push(part)
                is ExpressionPart.RightBracket -> {
                    while (!stack.empty() && stack.peek().priority() > 0) {
                        postFixExpression.add(stack.pop())
                    }
                    try {
                        stack.pop()
                    } catch (e: EmptyStackException) {
                        throw IncorrectExpressionException("'(' missing")
                    }
                    if (!stack.empty() && stack.peek().priority() == 5) {
                        postFixExpression.add(stack.pop())
                    }
                }
                is ExpressionPart.Semicolon -> {
                    while (!stack.empty() && stack.peek().priority() > 0) {
                        postFixExpression.add(stack.pop())
                    }
                }
            }
        }
        while (!stack.empty()) {
            postFixExpression.add(stack.pop())
        }
        return postFixExpression.filterIsInstance<ExpressionPart.RpnPart>()
    }

    private fun ExpressionPart.priority() =
        when (this) {
            is ExpressionPart.LeftBracket -> 0
            is ExpressionPart.RpnPart.Operator -> this.operator.priority
            else -> throw IncorrectExpressionException("$this has no priority")
        }
}


