package com.github.kikimanjaro.stickyscroll.ui

import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.util.Weighted
import com.intellij.ui.LightweightHint
import java.awt.Color
import java.awt.Cursor
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JLayer
import javax.swing.JPanel

class StickyPanel(editor: EditorImpl, val hint: LightweightHint, val line: Int) {
    init {
        hint.component.cursor = Cursor(Cursor.HAND_CURSOR)
        hint.component.putClientProperty(FileEditorManager.SEPARATOR_DISABLED, true)
        hint.component.addMouseListener(object : MouseListener {
            override fun mouseClicked(e: MouseEvent?) {
                editor.scrollingModel.scrollTo(LogicalPosition(line, 0), ScrollType.RELATIVE);
            }

            override fun mousePressed(e: MouseEvent?) {}

            override fun mouseReleased(e: MouseEvent?) {}

            override fun mouseEntered(e: MouseEvent?) {}

            override fun mouseExited(e: MouseEvent?) {}

        })
    }

}