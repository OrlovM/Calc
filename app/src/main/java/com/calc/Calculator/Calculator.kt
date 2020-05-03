package com.calc.Calculator

class Calculator {
    fun calculate(expressionString: String): String {
        val rx = """.0$""".toRegex()
        val b = Parser().parse(expressionString)
        val c = ShuntingYardAlgorithm().makePostFix(b)
        val d = EvaluateRPN().calculateRpn(c)
        return d.toString().replace(rx, "")

    }
}