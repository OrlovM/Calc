package com.example.calc


class Lexer {
    private val regex = """(?<=\(|^)\-\d+\.?\d*|\d+\.?\d*|\+|\-|\*|\/|\^|\(|\)|[a-z]+""".toRegex()
    fun tokenize(expressionString: String): ArrayList<String> {
        var matchedTokens = regex.findAll(expressionString)
        val expressionTokenized = ArrayList<String>()
        for (token in matchedTokens) {
            expressionTokenized.add(token.value)
        }
        return expressionTokenized
    }
}