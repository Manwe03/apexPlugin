package com.manwe.apex.syntax

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

object ApexTokenTypes {
    @JvmField val WHITE_SPACE: IElementType = TokenType.WHITE_SPACE
    @JvmField val BAD_CHARACTER: IElementType = TokenType.BAD_CHARACTER

    @JvmField val KEYWORD = ApexTokenType("KEYWORD")
    @JvmField val TYPE_NAME = ApexTokenType("TYPE_NAME")
    @JvmField val PRIMITIVE_TYPE = ApexTokenType("PRIMITIVE_TYPE")
    @JvmField val SOBJECT_TYPE = ApexTokenType("SOBJECT_TYPE")
    @JvmField val ANNOTATION = ApexTokenType("ANNOTATION")
    
    @JvmField val STRING = ApexTokenType("STRING")
    @JvmField val NUMBER = ApexTokenType("NUMBER")
    @JvmField val BOOLEAN_LITERAL = ApexTokenType("BOOLEAN_LITERAL")
    @JvmField val NULL_LITERAL = ApexTokenType("NULL_LITERAL")
    
    @JvmField val LINE_COMMENT = ApexTokenType("LINE_COMMENT")
    @JvmField val BLOCK_COMMENT = ApexTokenType("BLOCK_COMMENT")
    @JvmField val DOC_COMMENT = ApexTokenType("DOC_COMMENT")
    
    @JvmField val IDENTIFIER = ApexTokenType("IDENTIFIER")
    
    @JvmField val OPERATOR = ApexTokenType("OPERATOR")
    @JvmField val DOT = ApexTokenType("DOT")
    @JvmField val COMMA = ApexTokenType("COMMA")
    @JvmField val SEMICOLON = ApexTokenType("SEMICOLON")
    @JvmField val COLON = ApexTokenType("COLON")
    @JvmField val QUESTION = ApexTokenType("QUESTION")
    @JvmField val AT = ApexTokenType("AT")
    @JvmField val ARROW = ApexTokenType("ARROW")
    
    @JvmField val LPAREN = ApexTokenType("LPAREN")
    @JvmField val RPAREN = ApexTokenType("RPAREN")
    @JvmField val LBRACE = ApexTokenType("LBRACE")
    @JvmField val RBRACE = ApexTokenType("RBRACE")
    @JvmField val LBRACKET = ApexTokenType("LBRACKET")
    @JvmField val RBRACKET = ApexTokenType("RBRACKET")

    val KEYWORDS = setOf(
        "abstract", "after", "and", "any", "array", "as", "asc", "before", "break",
        "by", "catch", "class", "continue", "delete", "desc", "do", "else", "enum",
        "extends", "false", "final", "finally", "for", "from", "global", "group",
        "having", "if", "implements", "in", "inherited", "insert", "instanceof",
        "interface", "into", "like", "limit", "merge", "new", "not", "null", "offset",
        "on", "or", "order", "override", "private", "protected", "public", "return",
        "select", "sharing", "static", "super", "switch", "testmethod", "this",
        "throw", "transient", "trigger", "true", "try", "undelete", "update", "upsert",
        "virtual", "void", "when", "where", "while", "with", "without"
    )

    val PRIMITIVE_TYPES = setOf(
        "blob", "boolean", "date", "datetime", "decimal", "double", "id", "integer",
        "long", "object", "string", "time", "void"
    )

    val SALESFORCE_SOBJECT_TYPES = setOf(
        "account", "contact", "opportunity", "lead", "case", "asset", "user",
        "userrole", "profile", "group", "task", "event", "attachment", "contentversion",
        "apexclass", "apexpage", "apexcomponent", "apextrigger", "organization", "site",
        "sessionheader", "sobject", "schema", "pagereference", "database", "system", "test",
        "list", "set", "map"
    )
}
