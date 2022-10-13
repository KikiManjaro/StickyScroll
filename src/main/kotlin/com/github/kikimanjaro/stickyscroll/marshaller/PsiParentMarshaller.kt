package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

interface PsiParentMarshaller {

    fun getParents(psiElement: PsiElement?): Sequence<PsiElement>?

    fun getTextRangeAndStartLine(element: PsiElement, document: Document) : Pair<TextRange, Int>
}