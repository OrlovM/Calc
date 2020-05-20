package com.calc.calculator

import android.util.Log

class Calculator {
    fun calculate(expressionString: String, metrics: Boolean = false): String {
        val rx = """.0$""".toRegex()
        if (expressionString == "") return expressionString
        val b = Parser().parse(expressionString)
        val c = ShuntingYardAlgorithm().makePostFix(b)
        val d = EvaluateRPN().calculateRpn(c, metrics)
        return d.toString().replace(rx, "")

    }
}