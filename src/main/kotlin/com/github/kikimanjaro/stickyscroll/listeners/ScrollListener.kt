package com.github.kikimanjaro.stickyscroll.listeners

import com.github.kikimanjaro.stickyscroll.StickyPanelManager
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.event.VisibleAreaEvent
import com.intellij.openapi.editor.event.VisibleAreaListener
import com.intellij.openapi.editor.impl.DocumentMarkupModel
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.util.parentsOfType
import com.intellij.util.ui.UIUtil
import org.jetbrains.kotlin.idea.editor.fixers.startLine
import org.jetbrains.kotlin.psi.KtClassOrObject
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
//        val parent = findInterestingParent(currentElement)
        val parents = currentElement?.parentsOfType<KtClassOrObject>()
        val parentsOther = currentElement?.parentsOfType<PsiClass>()
//        PsiUtilCore.getName(parent)
//        currentElement?.parents(false)?.toList()?.get(1)?.text
        stickyPanelManager.clearPanelList()
        if (parents != null) {
            for (parent in parents.toList().reversed()) {
                val parentLine = parent.startLine(document)
                val start = document.getLineStartOffset(parentLine);
                val end = document.getLineEndOffset(parentLine)
                val realText = document.getText(TextRange(start, end))
                stickyPanelManager.addPanel(realText, parentLine + 1)

//                stickyPanelManager.editor.filteredDocumentMarkupModel.processRangeHighlightersOverlappingWith(start, end) {
//                    val textAttributes = it.getTextAttributes(stickyPanelManager.editor.colorsScheme)
//                    val foregroundColor = textAttributes?.foregroundColor
//                    //TODO: else default color
//                    return@processRangeHighlightersOverlappingWith true
//                }

                val text = document.immutableCharSequence
                val hlIter = stickyPanelManager.editor.highlighter.createIterator(0)
                val color by lazy(LazyThreadSafetyMode.NONE) {
                    try {
                        hlIter.textAttributes.foregroundColor
                    } catch (_: ConcurrentModificationException) {
                        null
                    }
                }
                var x = 0
                var y = 0
                val moveCharIndex = { code: Int, enterAction: (() -> Unit)? ->
                    when (code) {
                        9 -> x += 4//TAB
                        10 -> {//ENTER
                            x = 0
                            y = 1 //Changed
                            enterAction?.invoke()
                        }

                        else -> x += 1
                    }
                }
                val hasBlockInlay = stickyPanelManager.editor.inlayModel.hasBlockElements()

//                val highlightList = getHighlightColor(0, )
//                for (offset in start until end) {
//                    if (offset >= text.length) break
//                    val charCode = text[offset].code
//                    setColorRgba(highlightList.firstOrNull {
//                        offset >= it.startOffset && offset < it.endOffset
//                    }?.foregroundColor ?: color ?: defaultColor)
//                }

                val wordColor = HashMap<String, Color>()
                val allWordColors = HashMap<String, Color>()
                stickyPanelManager.editor.filteredDocumentMarkupModel.processRangeHighlightersOverlappingWith(
                    0,
                    document.getLineEndOffset(document.lineCount - 1)
                ) {
                    val word = document.getText(TextRange(it.startOffset, it.endOffset))
                    allWordColors[word] =
                        it.getTextAttributes(stickyPanelManager.editor.colorsScheme)?.foregroundColor
                            ?: defaultColor
                    if (it.startOffset in start - 1..end + 1) {
                        val word = document.getText(TextRange(it.startOffset, it.endOffset))
                        wordColor[word] =
                            it.getTextAttributes(stickyPanelManager.editor.colorsScheme)?.foregroundColor
                                ?: defaultColor
                    }
                    true
                }

                val wordColor2 = HashMap<String, Color>()
                val allWordColors2 = HashMap<String, Color>()
                stickyPanelManager.editor.filteredDocumentMarkupModel.processRangeHighlightersOutside(
                    0,
                    document.getLineEndOffset(document.lineCount - 1)
                ) {
                    val word = document.getText(TextRange(it.startOffset, it.endOffset))
                    allWordColors2[word] =
                        it.getTextAttributes(stickyPanelManager.editor.colorsScheme)?.foregroundColor
                            ?: defaultColor
                    if (it.startOffset in start - 1..end + 1) {
                        val word = document.getText(TextRange(it.startOffset, it.endOffset))
                        wordColor2[word] =
                            it.getTextAttributes(stickyPanelManager.editor.colorsScheme)?.foregroundColor
                                ?: defaultColor
                    }
                    true
                }

                val allWordColors3 = HashMap<String, Color>()
                val markupModel = DocumentMarkupModel.forDocument(document, stickyPanelManager.project, true)
                markupModel.allHighlighters.forEach {
                    val word = document.getText(TextRange(it.startOffset, it.endOffset))
                    allWordColors3[word] =
                        it.getTextAttributes(stickyPanelManager.editor.colorsScheme)?.foregroundColor
                            ?: defaultColor
                }


                val allHighlighters = stickyPanelManager.editor.markupModel.allHighlighters
                stickyPanelManager.editor.composedTextRange
//                allHighlighters[0].getTextAttributes(stickyPanelManager.editor.colorsScheme)?.foregroundColor
//                allHighlighters[0].startOffset
                UIUtil.getTreeTextBackground()
//                stickyPanelManager.editor.highlighter.
//                val toRemove = parent?.children?.last()?.text
//                val text = toRemove?.let { parent.text?.removeSuffix(it) }
//                val lines = StringReader(parent.text).readLines()
//                if (lines.isNotEmpty()) {
//                    stickyPanelManager.addPanel(lines[0], parent.startLine(stickyPanelManager.editor.document) + 1)
//                }
            }
        }
        stickyPanelManager.addTopLabels()

//        if (parent != null) {
//            stickyPanel.textLabel.text = PsiUtilCore.getName(parent)
//        } else {
//            stickyPanel.textLabel.text = ""
//        }


//        println(vOffset)
    }

//    fun findInterestingParent(currentElement: PsiElement?): PsiElement? {
//        currentElement?.parents(false)?.iterator()?.let {
//            for (element in it) {
//                if (element.elementType != null && PsiUtilCore.getElementType(element).toString() == "CLASS") { //OLD WAY
//                    return element
//                }
//            }
//        }
//        return null
//    }


    override fun dispose() {
        stickyPanelManager.editor.scrollingModel.removeVisibleAreaListener(this)
    }
}

private data class RangeHighlightColor(val startOffset: Int, val endOffset: Int, val foregroundColor: Color)