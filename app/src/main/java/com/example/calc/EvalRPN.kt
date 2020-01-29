package com.example.calc

import java.util.*
import kotlin.collections.ArrayList





class EvalRPN {


    fun calculateRpn(rpn: ArrayList<FormulaPart>): Double {
        val stack = LinkedList<Double>()
        stack.push(0.0)
        for (part in rpn) {
            when (part) {
                is FormulaPart.Value -> stack.addLast(part.value)
                is FormulaPart.RpnPart.Operator -> {
                    val result = part.operator.process(stack)
                    stack.addLast(result)
                }
            }
        }

        return stack.removeLast()
    }
}