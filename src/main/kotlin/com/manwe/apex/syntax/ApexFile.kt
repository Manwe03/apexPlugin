package com.manwe.apex.syntax

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.manwe.apex.ApexFileType
import com.manwe.apex.ApexLanguage

class ApexFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, ApexLanguage) {
    override fun getFileType(): FileType = ApexFileType.INSTANCE
    override fun toString(): String = "Apex File"
}
