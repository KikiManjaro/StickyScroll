package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset

abstract class DefaultTextRangeMarshaller : PsiParentMarshaller {
    override fun getTextRangeAndStartLine(element: PsiElement, document: Document): Pair<TextRange, Int> {
        val parentStartOffset = element.startOffset
//        val parentLine = document.getLineNumber(parentStartOffset)
//                val parentLine = parent.startLine(document)
//                val parentEndLine = parent.endLine(document)
//                val start = document.getLineStartOffset(parentLine);
        val firstChildOffset: Int =
            if (element.firstChild.startOffset == parentStartOffset && element.children.size > 1) {
                element.firstChild.nextSibling.endOffset
            } else {
                element.firstChild.endOffset
            }
        return Pair(TextRange(parentStartOffset, firstChildOffset), document.getLineNumber(parentStartOffset))
//        val realText = document.getText(textRange)
    }
}