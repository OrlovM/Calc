package com.example.calc



class Calculator {
    fun calculate(expressionString: String): String {
        val rx = """.0$""".toRegex()
        val a = Lexer().tokenize(expressionString)
        val b = Parser2().parse(a)
        val c = ShuntingYardAlgorithm2().makePostFix(b)
        val d = EvalRPN2().calculateRpn(c)
        return d.toString().replace(rx, "")
    }
}