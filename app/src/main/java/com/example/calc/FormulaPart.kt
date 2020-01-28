package com.example.calc

sealed class FormulaPart {
    object LeftBracket : FormulaPart() // надо для парсинга, но не надо для вычисленя
    object RightBracket : FormulaPart()
    sealed class RpnPart: FormulaPart() {
        data class Value(val value: Double) : RpnPart()
        data class Operator(val operator: FormulaOperator): RpnPart()
    }
}