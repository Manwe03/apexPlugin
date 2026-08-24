package com.manwe.apex.syntax

import com.intellij.psi.tree.IElementType
import com.manwe.apex.ApexLanguage

class ApexTokenType(debugName: String) : IElementType(debugName, ApexLanguage) {
    override fun toString(): String = "ApexTokenType.${super.toString()}"
}
