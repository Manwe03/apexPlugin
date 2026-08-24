package com.manwe.apex.syntax

import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.manwe.apex.ApexLanguage

object ApexElementTypes {
    @JvmField val FILE = IFileElementType("APEX_FILE", ApexLanguage)
    
    @JvmField val CLASS = ApexElementType("CLASS")
    @JvmField val INTERFACE = ApexElementType("INTERFACE")
    @JvmField val ENUM = ApexElementType("ENUM")
    @JvmField val METHOD = ApexElementType("METHOD")
    @JvmField val FIELD = ApexElementType("FIELD")
    @JvmField val VARIABLE = ApexElementType("VARIABLE")
    @JvmField val PARAMETER = ApexElementType("PARAMETER")
    @JvmField val METHOD_CALL = ApexElementType("METHOD_CALL")
    @JvmField val SOQL_EXPRESSION = ApexElementType("SOQL_EXPRESSION")
}

class ApexElementType(debugName: String) : IElementType(debugName, ApexLanguage)
