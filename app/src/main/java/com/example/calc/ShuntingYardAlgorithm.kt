package com.example.calc

import java.util.*


class ShuntingYardAlgorithm {
    fun makePostFix(formula: ArrayList<FormulaPart>): ArrayList<FormulaPart.RpnPart> {
        val postFixExpression = ArrayList<FormulaPart.RpnPart>()
        val stack = Stack<FormulaPart.RpnPart.Operator>()
        formula.forEach { part ->
            when (part) {
                is FormulaPart.RpnPart.Value -> postFixExpression.add(part)
                is FormulaPart.RpnPart.Operator -> {
                    if (stack.empty()) {
                        stack.push(part)
                    }
                    else {
                        while (!stack.empty() && stack.peek().operator.priority >= part.operator.priority) {
                            postFixExpression.add(stack.pop())
                        }
                        stack.push(part)
                    }
                }
                //is FormulaPart.LeftBracket -> stack.push(part)
            }

        }
        while (!stack.empty()) {
            postFixExpression.add(stack.pop())
        }
        return postFixExpression
    }
}