package com.github.kikimanjaro.stickyscroll.listeners

import com.github.kikimanjaro.stickyscroll.StickyPanelManager
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.event.VisibleAreaEvent
import com.intellij.openapi.editor.event.VisibleAreaListener
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.util.parents
import com.intellij.psi.util.parentsOfType
import com.intellij.ui.LightweightHint
import com.intellij.util.ui.UIUtil
import org.jetbrains.kotlin.idea.editor.fixers.endLine
import org.jetbrains.kotlin.idea.editor.fixers.startLine
import org.jetbrains.kotlin.psi.KtCallableDeclaration
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtDeclarationWithBody
import org.jetbrains.kotlin.psi.KtNamedDeclaration
import java.awt.Color
import java.awt.Point

class ScrollListener(val stickyPanelManager: StickyPanelManager) : VisibleAreaListener, Disposable {

    init {
        stickyPanelManager.editor.scrollingModel.addVisibleAreaListener(this, stickyPanelManager)
    }

    val scaleBuffer = FloatArray(4)
    val setColorRgba = { color: Color ->
        scaleBuffer[0] = color.red.toFloat()
        scaleBuffer[1] = color.green.toFloat()
        scaleBuffer[2] = color.blue.toFloat()
        scaleBuffer[3] = color.alpha.toFloat()
    }
    val defaultColor = stickyPanelManager.editor.colorsScheme.defaultForeground
    val hintList = ArrayList<LightweightHint>()

    override fun visibleAreaChanged(e: VisibleAreaEvent) {
        var logicalPosition = stickyPanelManager.editor.xyToLogicalPosition(
            Point(
                stickyPanelManager.editor.scrollingModel.visibleArea.width,
                stickyPanelManager.editor.scrollingModel.visibleArea.y
            )
        )
        runCatching { logicalPosition = LogicalPosition(logicalPosition.line - 1, logicalPosition.column) }


        val positionToOffset = stickyPanelManager.editor.logicalPositionToOffset(logicalPosition);

        val document = stickyPanelManager.editor.document
        val psiFile: PsiFile? =
            PsiDocumentManager.getInstance(stickyPanelManager.project).getPsiFile(document)
        val currentElement = psiFile?.findElementAt(positionToOffset - 1)

        val parents = currentElement?.parentsOfType<KtClassOrObject>()
        val parents2 = currentElement?.parents(false)?.filter { element -> element is KtDeclarationWithBody || element is KtClassOrObject }
        val parentsOther = currentElement?.parentsOfType<PsiClass>()

        stickyPanelManager.clearPanelList()
        var yDelta = 0
        var newLightweightHint = ArrayList<LightweightHint>()
        if (parents2 != null) {
            for (parent in parents2.toList().reversed()) {
                val parentLine = parent.startLine(document)
                val parentEndLine = parent.endLine(document)
                val start = document.getLineStartOffset(parentLine);
                val end = document.getLineEndOffset(parentLine)
                val textRange = TextRange(start, end)
                val realText = document.getText(textRange)

                val hint = MyEditorFragmentComponent.showEditorFragmentHint(
                    stickyPanelManager.editor,
                    textRange,
                    true,
                    false,
                    yDelta * stickyPanelManager.editor.lineHeight
                )
                stickyPanelManager.addPanel(hint!!, parentLine)


            }
        }
        stickyPanelManager.addTopLabels()
    }

    override fun dispose() {
        stickyPanelManager.editor.scrollingModel.removeVisibleAreaListener(this)
    }
}

private data class RangeHighlightColor(val startOffset: Int, val endOffset: Int, val foregroundColor: Color)