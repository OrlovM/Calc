package com.example.calc


class Lexer {
    private val regex = """(?<=\(|^)-\d+\.?\d*|\d+\.?\d*|\+|-|\*|/|\^|\(|\)|[a-z]+|,""".toRegex()
    private val multiplierRegex = """(?<=\d)(\(|[a-z])""".toRegex()
    fun tokenize(expressionString: String): ArrayList<String> {
        val matchedTokens = regex.findAll(getMultipier(expressionString))
        val expressionTokenized = ArrayList<String>()
        for (token in matchedTokens) {
            expressionTokenized.add(token.value)
        }
        return expressionTokenized
    }
    private fun getMultipier(expressionString: String): String {
        return expressionString.replace(multiplierRegex, "*$1")
    }
}