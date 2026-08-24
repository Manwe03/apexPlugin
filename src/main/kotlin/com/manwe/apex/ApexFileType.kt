package com.manwe.apex

import com.intellij.icons.AllIcons
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class ApexFileType private constructor() : LanguageFileType(ApexLanguage) {
    override fun getName(): String = "Apex"
    override fun getDescription(): String = "Salesforce Apex source file"
    override fun getDefaultExtension(): String = "cls"
    override fun getIcon(): Icon = AllIcons.FileTypes.Java

    companion object {
        @JvmStatic
        val INSTANCE = ApexFileType()
    }
}
