package com.calc.calculator

sealed class ExpressionPart {
    object LeftBracket : ExpressionPart()
    object RightBracket : ExpressionPart()
    object Semicolon: ExpressionPart()
    sealed class RpnPart: ExpressionPart() {
        data class Value(val value: Double) : RpnPart()
        data class Operator(val operator: ExpressionOperator): RpnPart()
    }
}