package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset

abstract class DefaultTextRangeMarshaller : PsiParentMarshaller {
    override fun getTextRangeAndStartLine(element: PsiElement, document: Document): Pair<TextRange, Int> {
        val parentStartOffset = element.startOffset
        val firstChild = element.firstChild
        // Defensive: elements without children (e.g. synthetic PSI) — fall back to element's own range
        if (firstChild == null) {
            return Pair(element.textRange, document.getLineNumber(parentStartOffset))
        }
        val firstChildOffset: Int = runCatching {
            val isSameOffset = firstChild.startOffset == parentStartOffset
            val hasSibling = element.children.size > 1 && firstChild.nextSibling != null
            if (isSameOffset && hasSibling) {
                firstChild.nextSibling!!.endOffset
            } else {
                firstChild.endOffset
            }
        }.getOrDefault(firstChild.endOffset)
        // Clamp to document bounds
        val safeEnd = firstChildOffset.coerceIn(parentStartOffset, document.textLength)
        return Pair(TextRange(parentStartOffset, safeEnd), document.getLineNumber(parentStartOffset))
    }
}