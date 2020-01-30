package com.example.calc

class Parser {
    private val regex = """-?\d+\.?\d*""".toRegex()
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
        "sqrt" to FormulaPart.RpnPart.Operator(FormulaOperator.SQRT),
        "(" to FormulaPart.RpnPart.LeftBracket,
        ")" to FormulaPart.RightBracket,
        "," to FormulaPart.Comma

    )


    fun parse (expressionTokenized: ArrayList<String>): ArrayList<FormulaPart> {
        var formula = ArrayList<FormulaPart>()
        expressionTokenized.forEach { token ->
            if (regex.containsMatchIn(token)) {
                formula.add(FormulaPart.Value(token.toDouble()))
            }
            else {
                try {
                    formula.add(map.getValue(token))
                } catch (e: NoSuchElementException) {
                    throw IncorrectExpressionException("No such operator or function")
                }
            }

        }
        //formula.dropLastWhile {it is FormulaPart.RpnPart.Operator }

        return formula
    }

}