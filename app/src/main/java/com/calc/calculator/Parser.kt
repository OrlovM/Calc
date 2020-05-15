package com.calc.calculator

class Parser {

    private val tokenRegex = """\d+\.?\d*|\+|-|\*|/|\^|\—|\(|\)|[A-z]+|;""".toRegex()
    private val multiplierRegex = """((?<=\d)(\(|[a-z])|(?<=\))(\d|[A-z])|(?<=\))(\())""".toRegex()
    private val numberRegex = """-?\d+\.?\d*""".toRegex()
    private val unaryMinusRegex = """((?<=\(|^|\+|\-|\*|\/)-)""".toRegex()
    private val tokenMap = mapOf(
        "+" to ExpressionPart.RpnPart.Operator(ExpressionOperator.PLUS),
        "-" to ExpressionPart.RpnPart.Operator(ExpressionOperator.MINUS),
        "*" to ExpressionPart.RpnPart.Operator(ExpressionOperator.MUL),
        "/" to ExpressionPart.RpnPart.Operator(ExpressionOperator.DIV),
        "^" to ExpressionPart.RpnPart.Operator(ExpressionOperator.EXP),
        "%" to ExpressionPart.RpnPart.Operator(ExpressionOperator.PROC),
        "sin" to ExpressionPart.RpnPart.Operator(ExpressionOperator.SIN),
        "cos" to ExpressionPart.RpnPart.Operator(ExpressionOperator.COS),
        "tan" to ExpressionPart.RpnPart.Operator(ExpressionOperator.TAN),
        "cot" to ExpressionPart.RpnPart.Operator(ExpressionOperator.COT),
        "atan" to ExpressionPart.RpnPart.Operator(ExpressionOperator.ATAN),
        "root" to ExpressionPart.RpnPart.Operator(ExpressionOperator.ROOT),
        "sqrt" to ExpressionPart.RpnPart.Operator(ExpressionOperator.SQRT),
        "—" to ExpressionPart.RpnPart.Operator(ExpressionOperator.UNARYMINUS),
        "(" to ExpressionPart.LeftBracket,
        ")" to ExpressionPart.RightBracket,
        ";" to ExpressionPart.Semicolon
    )

    private fun String.tokenize(): List<String> {
        val matchedTokens = tokenRegex.findAll(this.getMultiplier())
        val expressionTokenized = ArrayList<String>()
        for (token in matchedTokens) {
            expressionTokenized.add(token.value)
        }
        return expressionTokenized
    }

    private fun String.getMultiplier(): String {
        return this
            .replace(multiplierRegex, "*$1")
            .replace(unaryMinusRegex, "—")
    }

    fun parse (expression: String): List<ExpressionPart> {
        return expression
            .tokenize()
            .map { token ->
                if (numberRegex.containsMatchIn(token)) {
                    ExpressionPart.RpnPart.Value(token.toDouble())
                }
                else {
                    tokenMap[token] ?: throw IncorrectExpressionException(
                        "No such operator or function: $token"
                    )
                }
            }
            .dropLastWhile { it is ExpressionPart.RpnPart.Operator || it is ExpressionPart.LeftBracket }
    }
}