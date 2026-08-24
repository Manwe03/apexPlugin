package com.manwe.apex.syntax

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

class ApexSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer = ApexLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return when (tokenType) {
            ApexTokenTypes.KEYWORD,
            ApexTokenTypes.BOOLEAN_LITERAL,
            ApexTokenTypes.NULL_LITERAL -> KEYWORD_KEYS

            ApexTokenTypes.TYPE_NAME -> TYPE_NAME_KEYS
            ApexTokenTypes.ANNOTATION -> ANNOTATION_KEYS

            ApexTokenTypes.STRING -> STRING_KEYS
            ApexTokenTypes.NUMBER -> NUMBER_KEYS

            ApexTokenTypes.LINE_COMMENT -> LINE_COMMENT_KEYS
            ApexTokenTypes.BLOCK_COMMENT -> BLOCK_COMMENT_KEYS
            ApexTokenTypes.DOC_COMMENT -> DOC_COMMENT_KEYS

            ApexTokenTypes.OPERATOR -> OPERATOR_KEYS
            ApexTokenTypes.DOT -> DOT_KEYS
            ApexTokenTypes.SEMICOLON -> SEMICOLON_KEYS
            ApexTokenTypes.COMMA -> COMMA_KEYS

            ApexTokenTypes.LPAREN, ApexTokenTypes.RPAREN -> PARENTHESES_KEYS
            ApexTokenTypes.LBRACE, ApexTokenTypes.RBRACE -> BRACES_KEYS
            ApexTokenTypes.LBRACKET, ApexTokenTypes.RBRACKET -> BRACKETS_KEYS

            ApexTokenTypes.BAD_CHARACTER -> BAD_CHAR_KEYS

            else -> EMPTY_KEYS
        }
    }

    companion object {
        val KEYWORD = createTextAttributesKey("APEX_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val TYPE_NAME = createTextAttributesKey("APEX_TYPE_NAME", DefaultLanguageHighlighterColors.CLASS_NAME)
        val CLASS_DECLARATION = createTextAttributesKey("APEX_CLASS_DECLARATION", DefaultLanguageHighlighterColors.CLASS_NAME)
        val METHOD_DECLARATION = createTextAttributesKey("APEX_METHOD_DECLARATION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION)
        val METHOD_CALL = createTextAttributesKey("APEX_METHOD_CALL", DefaultLanguageHighlighterColors.FUNCTION_CALL)
        val CONSTANT = createTextAttributesKey("APEX_CONSTANT", DefaultLanguageHighlighterColors.CONSTANT)
        val CUSTOM_FIELD = createTextAttributesKey("APEX_CUSTOM_FIELD", DefaultLanguageHighlighterColors.INSTANCE_FIELD)
        val SOQL_KEYWORD = createTextAttributesKey("APEX_SOQL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)

        val ANNOTATION = createTextAttributesKey("APEX_ANNOTATION", DefaultLanguageHighlighterColors.METADATA)
        val STRING = createTextAttributesKey("APEX_STRING", DefaultLanguageHighlighterColors.STRING)
        val NUMBER = createTextAttributesKey("APEX_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val LINE_COMMENT = createTextAttributesKey("APEX_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val BLOCK_COMMENT = createTextAttributesKey("APEX_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT)
        val DOC_COMMENT = createTextAttributesKey("APEX_DOC_COMMENT", DefaultLanguageHighlighterColors.DOC_COMMENT)
        val OPERATOR = createTextAttributesKey("APEX_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val PARENTHESES = createTextAttributesKey("APEX_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES)
        val BRACES = createTextAttributesKey("APEX_BRACES", DefaultLanguageHighlighterColors.BRACES)
        val BRACKETS = createTextAttributesKey("APEX_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS)
        val DOT = createTextAttributesKey("APEX_DOT", DefaultLanguageHighlighterColors.DOT)
        val SEMICOLON = createTextAttributesKey("APEX_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON)
        val COMMA = createTextAttributesKey("APEX_COMMA", DefaultLanguageHighlighterColors.COMMA)
        val BAD_CHARACTER = createTextAttributesKey("APEX_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)

        private val KEYWORD_KEYS = arrayOf(KEYWORD)
        private val TYPE_NAME_KEYS = arrayOf(TYPE_NAME)
        private val ANNOTATION_KEYS = arrayOf(ANNOTATION)
        private val STRING_KEYS = arrayOf(STRING)
        private val NUMBER_KEYS = arrayOf(NUMBER)
        private val LINE_COMMENT_KEYS = arrayOf(LINE_COMMENT)
        private val BLOCK_COMMENT_KEYS = arrayOf(BLOCK_COMMENT)
        private val DOC_COMMENT_KEYS = arrayOf(DOC_COMMENT)
        private val OPERATOR_KEYS = arrayOf(OPERATOR)
        private val PARENTHESES_KEYS = arrayOf(PARENTHESES)
        private val BRACES_KEYS = arrayOf(BRACES)
        private val BRACKETS_KEYS = arrayOf(BRACKETS)
        private val DOT_KEYS = arrayOf(DOT)
        private val SEMICOLON_KEYS = arrayOf(SEMICOLON)
        private val COMMA_KEYS = arrayOf(COMMA)
        private val BAD_CHAR_KEYS = arrayOf(BAD_CHARACTER)
        private val EMPTY_KEYS = emptyArray<TextAttributesKey>()
    }
}
