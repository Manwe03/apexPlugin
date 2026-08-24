package com.manwe.apex.syntax

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType

class ApexParser : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val rootMarker = builder.mark()
        
        while (!builder.eof()) {
            val tokenType = builder.tokenType
            
            if (tokenType == ApexTokenTypes.LBRACKET) {
                // Check if this bracket contains a SOQL/SOSL query e.g. [SELECT ...]
                val marker = builder.mark()
                builder.advanceLexer()
                var isSoql = false
                while (!builder.eof() && builder.tokenType != ApexTokenTypes.RBRACKET) {
                    val text = builder.tokenText?.lowercase()
                    if (text == "select" || text == "find" || text == "from") {
                        isSoql = true
                    }
                    builder.advanceLexer()
                }
                if (builder.tokenType == ApexTokenTypes.RBRACKET) {
                    builder.advanceLexer()
                }
                if (isSoql) {
                    marker.done(ApexElementTypes.SOQL_EXPRESSION)
                } else {
                    marker.drop()
                }
            } else {
                builder.advanceLexer()
            }
        }
        
        rootMarker.done(root)
        return builder.treeBuilt
    }
}
