package com.manwe.apex.syntax

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement

class ApexAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is LeafPsiElement) return

        val text = element.text
        if (text.isBlank()) return
        val lowerText = text.lowercase()

        // 1. Salesforce Custom Fields & Custom Objects (__c, __r, __e, __mdt, __share)
        if (text.contains("__c") || text.contains("__r") || text.contains("__e") || text.contains("__mdt") || text.contains("__share")) {
            highlight(element, holder, ApexSyntaxHighlighter.CUSTOM_FIELD)
            return
        }

        // 2. Class, Interface, Enum declaration names
        val prevSibling = getPrevNonWhitespaceSibling(element)
        val prevText = prevSibling?.text?.lowercase()
        if (prevText == "class" || prevText == "interface" || prevText == "enum") {
            highlight(element, holder, ApexSyntaxHighlighter.CLASS_DECLARATION)
            return
        }

        // 3. Method declarations (e.g. void getAccounts(...), List<Account> create(), Map<String, Contact> fetch())
        val nextSibling = getNextNonWhitespaceSibling(element)
        val nextText = nextSibling?.text
        if (nextText == "(" && isMethodDeclarationContext(element)) {
            highlight(element, holder, ApexSyntaxHighlighter.METHOD_DECLARATION)
            return
        }

        // 4. Method calls (e.g. debug(), getAccounts(), System.assert())
        if (nextText == "(") {
            highlight(element, holder, ApexSyntaxHighlighter.METHOD_CALL)
            return
        }

        // 5. Constants (ALL_CAPS format e.g. MAX_RECORDS, DEFAULT_LIMIT)
        if (text.length > 2 && text == text.uppercase() && text.any { it.isLetter() } && (text.contains("_") || isDeclaredConstant(element))) {
            highlight(element, holder, ApexSyntaxHighlighter.CONSTANT)
            return
        }

        // 6. Primitive Types (Integer, String, Boolean, Date, Datetime, Decimal, Double, Id, Long, Object, Void)
        if (ApexTokenTypes.PRIMITIVE_TYPES.contains(lowerText)) {
            highlight(element, holder, ApexSyntaxHighlighter.PRIMITIVE_TYPE)
            return
        }

        // 7. Salesforce SObject & Complex Types (Account, Contact, Opportunity, Lead, List, Set, Map, etc.)
        if (ApexTokenTypes.SALESFORCE_SOBJECT_TYPES.contains(lowerText) || (text.firstOrNull()?.isUpperCase() == true && text.all { it.isLetterOrDigit() || it == '_' })) {
            highlight(element, holder, ApexSyntaxHighlighter.SOBJECT_TYPE)
            return
        }

        // 8. SOQL Keywords inside [SELECT ...] queries
        if (isInSoqlQuery(element)) {
            if (SOQL_KEYWORDS.contains(lowerText)) {
                highlight(element, holder, ApexSyntaxHighlighter.SOQL_KEYWORD)
                return
            }
        }
    }

    private fun highlight(element: PsiElement, holder: AnnotationHolder, key: TextAttributesKey) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element.textRange)
            .textAttributes(key)
            .create()
    }

    private fun getPrevNonWhitespaceSibling(element: PsiElement): PsiElement? {
        var sibling = element.prevSibling
        while (sibling != null && sibling.text.isBlank()) {
            sibling = sibling.prevSibling
        }
        return sibling
    }

    private fun getNextNonWhitespaceSibling(element: PsiElement): PsiElement? {
        var sibling = element.nextSibling
        while (sibling != null && sibling.text.isBlank()) {
            sibling = sibling.nextSibling
        }
        return sibling
    }

    private fun isMethodDeclarationContext(element: PsiElement): Boolean {
        val prev = getPrevNonWhitespaceSibling(element) ?: return false
        val pText = prev.text.lowercase()

        // Preceded by closing bracket of generic type e.g. List<Account> create() or Map<K,V> process() or Account[] create()
        if (prev.text == ">" || prev.text == "]") {
            return true
        }

        // Preceded by void or return type name e.g. void create(), String getName(), Account getAccount()
        if (pText == "void" || ApexTokenTypes.PRIMITIVE_TYPES.contains(pText) || ApexTokenTypes.SALESFORCE_SOBJECT_TYPES.contains(pText) || prev.text.firstOrNull()?.isUpperCase() == true) {
            return true
        }

        // Check preceding modifier chain (public/private/static/override/etc.)
        var curr: PsiElement? = prev
        while (curr != null) {
            val t = curr.text.lowercase()
            if (t in listOf("public", "private", "protected", "global", "static", "override", "virtual", "abstract")) {
                return true
            }
            if (t == ";" || t == "{" || t == "}") break
            curr = getPrevNonWhitespaceSibling(curr)
        }

        return false
    }

    private fun isDeclaredConstant(element: PsiElement): Boolean {
        var p = getPrevNonWhitespaceSibling(element)
        while (p != null) {
            val t = p.text.lowercase()
            if (t == "final") return true
            if (t == ";" || t == "{" || t == "}") break
            p = getPrevNonWhitespaceSibling(p)
        }
        return false
    }

    private fun isInSoqlQuery(element: PsiElement): Boolean {
        var curr: PsiElement? = element
        while (curr != null) {
            if (curr.node?.elementType == ApexElementTypes.SOQL_EXPRESSION) {
                return true
            }
            curr = curr.parent
        }
        return false
    }

    companion object {
        private val SOQL_KEYWORDS = setOf(
            "select", "from", "where", "limit", "offset", "order", "by", "group", "having",
            "like", "in", "not", "asc", "desc", "nulls", "first", "last",
            "with", "security_enforced", "user_mode", "system_mode", "using", "scope"
        )
    }
}
