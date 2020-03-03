package com.example.calc

import java.util.*


class EvaluateRPN {

    fun calculateRpn(rpn: List<ExpressionPart.RpnPart>): Double {
        val stack = LinkedList<Double>()
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