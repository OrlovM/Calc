package com.example.calc

class Calculator(val inExpression: String) {
    fun calculate(): String {
        val postFix = ShuntingYardAlgorythm(inExpression).makePostFix()
        return PostFixEvaluate(postFix).evaluate()
    }
}