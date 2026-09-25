package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset

abstract class DefaultTextRangeMarshaller : PsiParentMarshaller {
    override fun getTextRangeAndStartLine(element: PsiElement, document: Document): Pair<TextRange, Int> {
        val firstChild = element.firstChild
        // Defensive: elements without children (e.g. synthetic PSI) — fall back to element's own range
        if (firstChild == null) {
            return Pair(element.textRange, document.getLineNumber(element.startOffset))
        }
        // Javadoc, KDoc and any other leading comment belongs to the declaration's text range but not to
        // its header: it can span dozens of lines and would then fill the whole pinned area with no room
        // left for the code being scrolled (#5). The fragment must start at the first line of code.
        val headerChild = firstNonCommentChildOf(element) ?: firstChild
        val headerStartOffset = headerChild.startOffset
        val headerEndOffset: Int = runCatching {
            // A header starts with a low-level token (a modifier list, or the '<' of an XML tag), so
            // extend the fragment to the token that follows it, e.g. "public class" or "<foo".
            val nextToken = headerChild.nextSibling
            if (nextToken != null) nextToken.endOffset else headerChild.endOffset
        }.getOrDefault(headerChild.endOffset)
        // Clamp to document bounds
        val safeEnd = headerEndOffset.coerceIn(headerStartOffset, document.textLength)
        return Pair(TextRange(headerStartOffset, safeEnd), document.getLineNumber(headerStartOffset))
    }

    /**
     * Returns the first child of [element] that is neither whitespace nor a comment — the first element
     * of its declaration header — or null when all of its children are whitespace/comments.
     */
    private fun firstNonCommentChildOf(element: PsiElement): PsiElement? {
        var child = element.firstChild
        while (child != null && (child is PsiComment || child is PsiWhiteSpace)) {
            child = child.nextSibling
        }
        return child
    }
}