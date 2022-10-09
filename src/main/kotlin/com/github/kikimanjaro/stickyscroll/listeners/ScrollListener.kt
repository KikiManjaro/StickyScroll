package com.github.kikimanjaro.stickyscroll.listeners

import com.github.kikimanjaro.stickyscroll.config.StickyScrollConfigService.Companion.ConfigInstance
import com.github.kikimanjaro.stickyscroll.marshaller.PsiParentMarshallerManager
import com.github.kikimanjaro.stickyscroll.services.StickyPanelManager
import com.github.kikimanjaro.stickyscroll.ui.MyEditorFragmentComponent
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.event.VisibleAreaEvent
import com.intellij.openapi.editor.event.VisibleAreaListener
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import org.jetbrains.kotlin.idea.editor.fixers.endLine
import org.jetbrains.kotlin.psi.psiUtil.startOffset
import java.awt.Point

class ScrollListener(val stickyPanelManager: StickyPanelManager) : VisibleAreaListener, Disposable {

    val editor = stickyPanelManager.editor

    init {
        editor.scrollingModel.addVisibleAreaListener(this, stickyPanelManager)
    }

    override fun visibleAreaChanged(e: VisibleAreaEvent) {
        var logicalPosition = editor.xyToLogicalPosition(
            Point(
                editor.scrollingModel.visibleArea.width,
                editor.scrollingModel.visibleArea.y
            )
        )
        runCatching { logicalPosition = LogicalPosition(logicalPosition.line - 1, logicalPosition.column) }


        val positionToOffset = editor.logicalPositionToOffset(logicalPosition);
        val document = editor.document
        stickyPanelManager.clearPanelList()
        if (document.getLineNumber(positionToOffset) > 0) {
            val psiFile: PsiFile? =
                PsiDocumentManager.getInstance(stickyPanelManager.project).getPsiFile(document)
            val currentElement = psiFile?.findElementAt(positionToOffset - 1)

            val parentMarshaller = PsiParentMarshallerManager.getParentMarshaller(psiFile?.language)

            val parents = parentMarshaller?.getParents(currentElement)
            parents?.toList()
            var yDelta = 0
            if (parents != null) {
                for (parent in parents.toList().reversed().take(ConfigInstance.state.maxLine)) {
                    val parentStartOffset = parent.startOffset
                    val parentLine = document.getLineNumber(parentStartOffset)
//                val parentLine = parent.startLine(document)
//                val parentEndLine = parent.endLine(document)
//                val start = document.getLineStartOffset(parentLine);
                    val firstChildOffset = document.getLineEndOffset(parent.firstChild.endLine(document))
//                val end = document.getLineEndOffset(parentLine)
                    val textRange = TextRange(parentStartOffset, firstChildOffset)
                    val realText = document.getText(textRange)

                    val hint = MyEditorFragmentComponent.showEditorFragmentHint(
                        editor,
                        textRange,
                        true,
                        false,
                        yDelta * editor.lineHeight
                    )
                    stickyPanelManager.addPanel(hint!!, parentLine)
                }
            }
            stickyPanelManager.addTopLabels()
        }
    }

    override fun dispose() {
        editor.scrollingModel.removeVisibleAreaListener(this)
    }
}