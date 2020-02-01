package com.example.calc

sealed class FormulaPart {
    data class Value(val value: Double) : FormulaPart()
    object RightBracket : FormulaPart()
    object Semicolon: FormulaPart()
    sealed class RpnPart(open var priority: Int): FormulaPart() {
        object LeftBracket : RpnPart(0)
        data class Operator(val operator: FormulaOperator): RpnPart(priority = operator.priority)
    }
}