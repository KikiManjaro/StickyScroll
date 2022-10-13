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


        val positionToOffset = editor.logicalPositionToOffset(logicalPosition);
        val document = editor.document
        stickyPanelManager.clearPanelList()
        if (document.getLineNumber(positionToOffset) > 0) {
            val psiFile: PsiFile? = PsiDocumentManager.getInstance(stickyPanelManager.project).getPsiFile(document)
            val currentElement = psiFile?.findElementAt(positionToOffset - 1)

            val parentMarshaller = PsiParentMarshallerManager.getParentMarshaller(psiFile?.language)

            val parents = parentMarshaller?.getParents(currentElement)
            parents?.toList()
            var yDelta = 0
            if (parents != null) {
                yDelta += 1
                for (parent in parents.toList().reversed().take(ConfigInstance.state.maxLine)) {
                   val result = parentMarshaller.getTextRangeAndStartLine(parent, document)

                    val hint = MyEditorFragmentComponent.showEditorFragmentHint(
                        editor, result.first, true, false, yDelta * editor.lineHeight
                    )
                    hint?.let { stickyPanelManager.addPanel(it, result.second) }
                }
            }
            stickyPanelManager.addTopLabels()
        }
    }

    override fun dispose() {
        editor.scrollingModel.removeVisibleAreaListener(this)
    }
}