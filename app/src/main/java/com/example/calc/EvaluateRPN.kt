package com.example.calc

import java.util.*


class EvaluateRPN {

    fun calculateRpn(rpn: List<ExpressionPart.RpnPart>): Double {
        val stack = LinkedList<Double>()
        stack.push(0.0)
        for (part in rpn) {
            val result: Double = when (part) {
                is ExpressionPart.RpnPart.Value -> part.value
                is ExpressionPart.RpnPart.Operator -> part.operator.process(stack)
            }
            stack.addLast(result)
        }
        return stack.removeLast()
    }
}