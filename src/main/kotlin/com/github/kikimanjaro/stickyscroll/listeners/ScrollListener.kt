package com.github.kikimanjaro.stickyscroll.listeners

import com.github.kikimanjaro.stickyscroll.config.StickyScrollConfigService.Companion.ConfigInstance
import com.github.kikimanjaro.stickyscroll.marshaller.PsiParentMarshallerManager
import com.github.kikimanjaro.stickyscroll.services.StickyPanelManager
import com.github.kikimanjaro.stickyscroll.ui.MyEditorFragmentComponent
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.event.VisibleAreaEvent
import com.intellij.openapi.editor.event.VisibleAreaListener
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import java.awt.Point

class ScrollListener(val stickyPanelManager: StickyPanelManager) : VisibleAreaListener, Disposable {

    val editor = stickyPanelManager.editor

    init {
        editor.scrollingModel.addVisibleAreaListener(this, stickyPanelManager)
    }

    override fun visibleAreaChanged(e: VisibleAreaEvent) {
        var logicalPosition = editor.xyToLogicalPosition(
            Point(
                editor.scrollingModel.visibleArea.width, editor.scrollingModel.visibleArea.y
            )
        )
        runCatching { logicalPosition = LogicalPosition(logicalPosition.line - 1, logicalPosition.column) }

        // Guard: Jupyter notebooks (.ipynb) are not supported and crash the plugin (#3)
        val document = editor.document
        val psiFile: PsiFile? = PsiDocumentManager.getInstance(stickyPanelManager.project).getPsiFile(document)
        if (psiFile != null && psiFile.name.endsWith(".ipynb", ignoreCase = true)) {
            stickyPanelManager.clearPanelList()
            return
        }

        val positionToOffset = try {
            editor.logicalPositionToOffset(logicalPosition)
        } catch (_: Exception) {
            stickyPanelManager.clearPanelList()
            return
        }
        stickyPanelManager.clearPanelList()
        if (positionToOffset < 0 || positionToOffset > document.textLength) return
        if (document.getLineNumber(positionToOffset) <= 0) return

        val currentElement = psiFile?.findElementAt((positionToOffset - 1).coerceAtLeast(0)) ?: return
        val parentMarshaller = PsiParentMarshallerManager.getParentMarshaller(psiFile.language) ?: return
        val parents = parentMarshaller.getParents(currentElement)?.toList().orEmpty()
        if (parents.isEmpty()) return

        var yDelta = 1
        for (parent in parents.reversed().take(ConfigInstance.state.maxLine)) {
            val result = runCatching { parentMarshaller.getTextRangeAndStartLine(parent, document) }.getOrNull() ?: continue
            val hint = MyEditorFragmentComponent.showEditorFragmentHint(
                editor, result.first, true, false, yDelta * editor.lineHeight
            )
            hint?.let { stickyPanelManager.addPanel(it, result.second) }
        }
        stickyPanelManager.addTopLabels()
    }

    override fun dispose() {
        editor.scrollingModel.removeVisibleAreaListener(this)
    }
}