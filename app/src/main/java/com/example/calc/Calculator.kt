package com.example.calc

import java.util.*
import kotlin.collections.ArrayList

class Calculator {
    fun calculate(expressionString: String): String {
        val a = Lexer().tokenize(expressionString)
        val b = Parser().parse(a)
        val c = ShuntingYardAlgorithm().makePostFix(b)
        val d = EvalRPN().calculateRpn(c)
        return d.toString()
    }

}