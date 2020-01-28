package com.example.calc

class Parser {
    private val regex = """\-?\d+\.?\d*""".toRegex()
    private val map = mapOf(
        "+" to FormulaPart.RpnPart.Operator(FormulaOperator.PLUS),
        "-" to FormulaPart.RpnPart.Operator(FormulaOperator.MINUS),
        "*" to FormulaPart.RpnPart.Operator(FormulaOperator.MUL),
        "/" to FormulaPart.RpnPart.Operator(FormulaOperator.DIV),
        "^" to FormulaPart.RpnPart.Operator(FormulaOperator.EXP),
        "%" to FormulaPart.RpnPart.Operator(FormulaOperator.PROC),
        "sin" to FormulaPart.RpnPart.Operator(FormulaOperator.SIN),
        "cos" to FormulaPart.RpnPart.Operator(FormulaOperator.COS),
        "tan" to FormulaPart.RpnPart.Operator(FormulaOperator.TAN),
        "cot" to FormulaPart.RpnPart.Operator(FormulaOperator.COT),
        "atan" to FormulaPart.RpnPart.Operator(FormulaOperator.ATAN),
        "sqr" to FormulaPart.RpnPart.Operator(FormulaOperator.SQR),
        "(" to FormulaPart.LeftBracket,
        ")" to FormulaPart.RightBracket
    )


    fun parse (expressionTokenized: ArrayList<String>): ArrayList<FormulaPart> {
        val formula = ArrayList<FormulaPart>()
        expressionTokenized.forEach { token ->
            if (regex.containsMatchIn(token)) {
                formula.add(FormulaPart.RpnPart.Value(token.toDouble()))
            }
            else {
                formula.add(map.getValue(token))
            }

        }
        return formula
    }

}