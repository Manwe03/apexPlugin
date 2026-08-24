package com.manwe.apex.syntax

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import java.util.Locale

class ApexLexer : LexerBase() {
    private var buffer: CharSequence = ""
    private var startOffset: Int = 0
    private var endOffset: Int = 0
    private var currentOffset: Int = 0
    private var tokenStart: Int = 0
    private var tokenEnd: Int = 0
    private var currentTokenType: IElementType? = null

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.currentOffset = startOffset
        advance()
    }

    override fun getState(): Int = 0

    override fun getTokenType(): IElementType? = currentTokenType

    override fun getTokenStart(): Int = tokenStart

    override fun getTokenEnd(): Int = tokenEnd

    override fun getBufferSequence(): CharSequence = buffer

    override fun getBufferEnd(): Int = endOffset

    override fun advance() {
        if (currentOffset >= endOffset) {
            tokenStart = endOffset
            tokenEnd = endOffset
            currentTokenType = null
            return
        }

        tokenStart = currentOffset
        val ch = buffer[currentOffset]

        // 1. Whitespace
        if (ch.isWhitespace()) {
            while (currentOffset < endOffset && buffer[currentOffset].isWhitespace()) {
                currentOffset++
            }
            tokenEnd = currentOffset
            currentTokenType = ApexTokenTypes.WHITE_SPACE
            return
        }

        // 2. Comments
        if (ch == '/' && currentOffset + 1 < endOffset) {
            val next = buffer[currentOffset + 1]
            if (next == '/') {
                // Line comment
                currentOffset += 2
                while (currentOffset < endOffset && buffer[currentOffset] != '\n' && buffer[currentOffset] != '\r') {
                    currentOffset++
                }
                tokenEnd = currentOffset
                currentTokenType = ApexTokenTypes.LINE_COMMENT
                return
            } else if (next == '*') {
                // Block comment or Doc comment
                val isDoc = currentOffset + 2 < endOffset && buffer[currentOffset + 2] == '*'
                currentOffset += 2
                while (currentOffset < endOffset) {
                    if (buffer[currentOffset] == '*' && currentOffset + 1 < endOffset && buffer[currentOffset + 1] == '/') {
                        currentOffset += 2
                        break
                    }
                    currentOffset++
                }
                tokenEnd = currentOffset
                currentTokenType = if (isDoc) ApexTokenTypes.DOC_COMMENT else ApexTokenTypes.BLOCK_COMMENT
                return
            }
        }

        // 3. Annotations (@AnnotationName)
        if (ch == '@') {
            currentOffset++
            if (currentOffset < endOffset && (buffer[currentOffset].isLetter() || buffer[currentOffset] == '_')) {
                while (currentOffset < endOffset && (buffer[currentOffset].isLetterOrDigit() || buffer[currentOffset] == '_')) {
                    currentOffset++
                }
                tokenEnd = currentOffset
                currentTokenType = ApexTokenTypes.ANNOTATION
                return
            }
            tokenEnd = currentOffset
            currentTokenType = ApexTokenTypes.AT
            return
        }

        // 4. Strings ('...')
        if (ch == '\'') {
            currentOffset++
            while (currentOffset < endOffset) {
                val c = buffer[currentOffset]
                if (c == '\\' && currentOffset + 1 < endOffset) {
                    currentOffset += 2
                    continue
                }
                if (c == '\'') {
                    currentOffset++
                    break
                }
                if (c == '\n' || c == '\r') {
                    break
                }
                currentOffset++
            }
            tokenEnd = currentOffset
            currentTokenType = ApexTokenTypes.STRING
            return
        }

        // 5. Numbers
        if (ch.isDigit()) {
            if (ch == '0' && currentOffset + 1 < endOffset && (buffer[currentOffset + 1] == 'x' || buffer[currentOffset + 1] == 'X')) {
                currentOffset += 2
                while (currentOffset < endOffset && buffer[currentOffset].isHexDigit()) {
                    currentOffset++
                }
            } else {
                while (currentOffset < endOffset && buffer[currentOffset].isDigit()) {
                    currentOffset++
                }
                if (currentOffset < endOffset && buffer[currentOffset] == '.' && currentOffset + 1 < endOffset && buffer[currentOffset + 1].isDigit()) {
                    currentOffset++
                    while (currentOffset < endOffset && buffer[currentOffset].isDigit()) {
                        currentOffset++
                    }
                }
                if (currentOffset < endOffset && (buffer[currentOffset] in "lLdDfF")) {
                    currentOffset++
                }
            }
            tokenEnd = currentOffset
            currentTokenType = ApexTokenTypes.NUMBER
            return
        }

        // 6. Identifiers, Keywords, Builtin Types, Primitives, SObjects
        if (ch.isLetter() || ch == '_') {
            while (currentOffset < endOffset && (buffer[currentOffset].isLetterOrDigit() || buffer[currentOffset] == '_')) {
                currentOffset++
            }
            tokenEnd = currentOffset
            val text = buffer.subSequence(tokenStart, tokenEnd).toString().lowercase(Locale.ROOT)
            currentTokenType = when {
                text == "true" || text == "false" -> ApexTokenTypes.BOOLEAN_LITERAL
                text == "null" -> ApexTokenTypes.NULL_LITERAL
                ApexTokenTypes.KEYWORDS.contains(text) -> ApexTokenTypes.KEYWORD
                ApexTokenTypes.PRIMITIVE_TYPES.contains(text) -> ApexTokenTypes.PRIMITIVE_TYPE
                ApexTokenTypes.SALESFORCE_SOBJECT_TYPES.contains(text) -> ApexTokenTypes.SOBJECT_TYPE
                else -> ApexTokenTypes.IDENTIFIER
            }
            return
        }

        // 7. Multi-character operators
        val remaining = endOffset - currentOffset
        if (remaining >= 3) {
            val s3 = buffer.subSequence(currentOffset, currentOffset + 3).toString()
            if (s3 == "===" || s3 == "!==") {
                currentOffset += 3
                tokenEnd = currentOffset
                currentTokenType = ApexTokenTypes.OPERATOR
                return
            }
        }
        if (remaining >= 2) {
            val s2 = buffer.subSequence(currentOffset, currentOffset + 2).toString()
            if (s2 in listOf("==", "!=", "<=", ">=", "+=", "-=", "*=", "/=", "++", "--", "&&", "||", "=>", "?.", "::")) {
                currentOffset += 2
                tokenEnd = currentOffset
                currentTokenType = ApexTokenTypes.OPERATOR
                return
            }
        }

        // 8. Single character delimiters and operators
        currentOffset++
        tokenEnd = currentOffset
        currentTokenType = when (ch) {
            '.' -> ApexTokenTypes.DOT
            ',' -> ApexTokenTypes.COMMA
            ';' -> ApexTokenTypes.SEMICOLON
            ':' -> ApexTokenTypes.COLON
            '?' -> ApexTokenTypes.QUESTION
            '(' -> ApexTokenTypes.LPAREN
            ')' -> ApexTokenTypes.RPAREN
            '{' -> ApexTokenTypes.LBRACE
            '}' -> ApexTokenTypes.RBRACE
            '[' -> ApexTokenTypes.LBRACKET
            ']' -> ApexTokenTypes.RBRACKET
            '=', '+', '-', '*', '/', '%', '<', '>', '!', '&', '|', '^', '~' -> ApexTokenTypes.OPERATOR
            else -> ApexTokenTypes.BAD_CHARACTER
        }
    }

    private fun Char.isHexDigit(): Boolean = this in '0'..'9' || this in 'a'..'f' || this in 'A'..'F'
}
