package com.github.kikimanjaro.stickyscroll.marshaller


import com.intellij.json.psi.JsonContainer
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parents
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset

class JsonParentMarshaller : PsiParentMarshaller {
    override fun getParents(psiElement: PsiElement?): Sequence<PsiElement>? {
        return psiElement?.parents(false)?.filter { element -> element is JsonContainer }
    }

    override fun getTextRangeAndStartLine(element: PsiElement, document: Document): Pair<TextRange, Int> {
        val parentStartOffset = element.startOffset
        val firstChildOffset =element.firstChild.endOffset
        return Pair(TextRange(parentStartOffset, firstChildOffset), document.getLineNumber(parentStartOffset))
    }
}