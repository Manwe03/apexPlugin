package com.manwe.apex.syntax

import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class ApexColorSettingsPage : ColorSettingsPage {

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = "Apex"

    override fun getIcon(): Icon = AllIcons.FileTypes.Java

    override fun getHighlighter(): SyntaxHighlighter = ApexSyntaxHighlighter()

    override fun getDemoText(): String = """
        /**
         * Sample Apex Class for Salesforce
         */
        @IsTest
        public with sharing class AccountController {
            private static final Integer MAX_RECORDS = 50;

            @AuraEnabled(cacheable=true)
            public static List<Account> getAccounts(String searchKey) {
                // Fetch accounts matching key
                if (String.isBlank(searchKey)) {
                    return new List<Account>();
                }
                return [SELECT Id, Name, Phone FROM Account WHERE Name LIKE :('%' + searchKey + '%') LIMIT :MAX_RECORDS];
            }
        }
    """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? = null

    companion object {
        private val DESCRIPTORS = arrayOf(
            AttributesDescriptor("Keyword", ApexSyntaxHighlighter.KEYWORD),
            AttributesDescriptor("Type Name", ApexSyntaxHighlighter.TYPE_NAME),
            AttributesDescriptor("Annotation", ApexSyntaxHighlighter.ANNOTATION),
            AttributesDescriptor("String", ApexSyntaxHighlighter.STRING),
            AttributesDescriptor("Number", ApexSyntaxHighlighter.NUMBER),
            AttributesDescriptor("Line Comment", ApexSyntaxHighlighter.LINE_COMMENT),
            AttributesDescriptor("Block Comment", ApexSyntaxHighlighter.BLOCK_COMMENT),
            AttributesDescriptor("Doc Comment", ApexSyntaxHighlighter.DOC_COMMENT),
            AttributesDescriptor("Operator", ApexSyntaxHighlighter.OPERATOR),
            AttributesDescriptor("Parentheses", ApexSyntaxHighlighter.PARENTHESES),
            AttributesDescriptor("Braces", ApexSyntaxHighlighter.BRACES),
            AttributesDescriptor("Brackets", ApexSyntaxHighlighter.BRACKETS),
            AttributesDescriptor("Dot", ApexSyntaxHighlighter.DOT),
            AttributesDescriptor("Semicolon", ApexSyntaxHighlighter.SEMICOLON),
            AttributesDescriptor("Comma", ApexSyntaxHighlighter.COMMA),
            AttributesDescriptor("Bad Character", ApexSyntaxHighlighter.BAD_CHARACTER)
        )
    }
}
