package com.example.calc



class Calculator {
    fun calculate(expressionString: String): String {
        val rx = """.0$""".toRegex()
        val a = Lexer().tokenize(expressionString)
        val b = Parser().parse(a)
        val c = ShuntingYardAlgorithm().makePostFix(b)
        val d = EvalRPN().calculateRpn(c)
        return d.toString().replace(rx, "")
    }
}