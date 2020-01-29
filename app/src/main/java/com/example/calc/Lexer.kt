package com.example.calc


class Lexer {
    private val regex = """(?<=\(|^)-\d+\.?\d*|\d+\.?\d*|\+|-|\*|/|\^|\(|\)|[a-z]+""".toRegex()
    private val multiplierRegex = """(?<=\d)(\(|[a-z])""".toRegex()
    fun tokenize(expressionString: String): ArrayList<String> {
        val expressionString2 = getMultipier(expressionString)
        val matchedTokens = regex.findAll(expressionString2)
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