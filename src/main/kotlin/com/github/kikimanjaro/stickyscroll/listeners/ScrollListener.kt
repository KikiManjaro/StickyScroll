package com.github.kikimanjaro.stickyscroll.listeners

import com.github.kikimanjaro.stickyscroll.config.StickyScrollConfigService.Companion.ConfigInstance
import com.github.kikimanjaro.stickyscroll.marshaller.PsiParentMarshallerManager
import com.github.kikimanjaro.stickyscroll.services.StickyPanelManager
import com.github.kikimanjaro.stickyscroll.ui.MyEditorFragmentComponent
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.event.VisibleAreaEvent
import com.intellij.openapi.editor.event.VisibleAreaListener
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import java.awt.Point

class ScrollListener(val stickyPanelManager: StickyPanelManager) : VisibleAreaListener, Disposable {

    val editor = stickyPanelManager.editor

    init {
        editor.scrollingModel.addVisibleAreaListener(this, stickyPanelManager)
    }

    override fun visibleAreaChanged(e: VisibleAreaEvent) {
        try {
            // Skip Jupyter notebooks and non-text files that can cause PSI crashes
            val document = editor.document
            val virtualFile = FileDocumentManager.getInstance().getFile(document)
            if (virtualFile != null) {
                val ext = virtualFile.extension?.lowercase()
                val name = virtualFile.name.lowercase()
                if (ext == "ipynb" || name.endsWith(".ipynb")) {
                    stickyPanelManager.clearPanelList()
                    return
                }
            }

            var logicalPosition = editor.xyToLogicalPosition(
                Point(
                    editor.scrollingModel.visibleArea.width, editor.scrollingModel.visibleArea.y
                )
            )
            runCatching { logicalPosition = LogicalPosition(logicalPosition.line - 1, logicalPosition.column) }

            val positionToOffset = try {
                editor.logicalPositionToOffset(logicalPosition)
            } catch (t: Throwable) {
                return
            }

            if (positionToOffset < 0 || positionToOffset > document.textLength) return

            stickyPanelManager.clearPanelList()
            if (document.getLineNumber(positionToOffset) > 0) {
                val psiFile: PsiFile? = try {
                    PsiDocumentManager.getInstance(stickyPanelManager.project).getPsiFile(document)
                } catch (t: Throwable) {
                    null
                }
                if (psiFile == null) return
                if (!psiFile.isPhysical && psiFile.virtualFile == null) return

                val offset = (positionToOffset - 1).coerceAtLeast(0)
                val currentElement = try {
                    psiFile.findElementAt(offset)
                } catch (t: Throwable) {
                    null
                }

                val parentMarshaller = try {
                    PsiParentMarshallerManager.getParentMarshaller(psiFile.language)
                } catch (t: Throwable) {
                    null
                } ?: return

                val parents = try {
                    parentMarshaller.getParents(currentElement)
                } catch (t: Throwable) {
                    null
                } ?: return

                var yDelta = 0
                val parentList = try {
                    parents.toList()
                } catch (t: Throwable) {
                    emptyList()
                }
                if (parentList.isNotEmpty()) {
                    yDelta += 1
                    for (parent in parentList.reversed().take(ConfigInstance.state.maxLine)) {
                        val result = try {
                            parentMarshaller.getTextRangeAndStartLine(parent, document)
                        } catch (t: Throwable) {
                            continue
                        }
                        val hint = try {
                            MyEditorFragmentComponent.showEditorFragmentHint(
                                editor, result.first, true, false, yDelta * editor.lineHeight
                            )
                        } catch (t: Throwable) {
                            null
                        }
                        hint?.let { stickyPanelManager.addPanel(it, result.second) }
                    }
                }
                stickyPanelManager.addTopLabels()
            }
        } catch (t: Throwable) {
            try {
                stickyPanelManager.clearPanelList()
            } catch (_: Throwable) {}
        }
    }

    override fun dispose() {
        try {
            editor.scrollingModel.removeVisibleAreaListener(this)
        } catch (_: Throwable) {}
    }
}
