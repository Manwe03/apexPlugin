package com.manwe.apex.syntax

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class ApexParserDefinition : ParserDefinition {

    override fun createLexer(project: Project?): Lexer = ApexLexer()

    override fun createParser(project: Project?): PsiParser = ApexParser()

    override fun getFileNodeType(): IFileElementType = ApexElementTypes.FILE

    override fun getCommentTokens(): TokenSet = TokenSet.create(
        ApexTokenTypes.LINE_COMMENT,
        ApexTokenTypes.BLOCK_COMMENT,
        ApexTokenTypes.DOC_COMMENT
    )

    override fun getStringLiteralElements(): TokenSet = TokenSet.create(
        ApexTokenTypes.STRING
    )

    override fun createElement(node: ASTNode): PsiElement = ASTWrapperPsiElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = ApexFile(viewProvider)
}
