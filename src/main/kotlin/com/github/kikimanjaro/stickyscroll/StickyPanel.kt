package com.github.kikimanjaro.stickyscroll

import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.util.Weighted
import java.awt.BorderLayout
import java.awt.Cursor
import java.awt.Font
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel

class StickyPanel(editor: EditorImpl, text: String, line: Int) : JPanel(),
    Weighted {

    init {
        this.layout = BorderLayout(0, 16)
        val insidePanel = JPanel(BorderLayout(40, 0))
        val lineLabel = JLabel(line.toString())
        lineLabel.font = editor.getFontMetrics(Font.PLAIN).font
        val textLabel = JLabel(text)
        textLabel.font = editor.getFontMetrics(Font.PLAIN).font
        insidePanel.add(lineLabel, BorderLayout.WEST)
        insidePanel.add(textLabel, BorderLayout.CENTER)
        insidePanel.border = BorderFactory.createEmptyBorder(0, 5, 0, 0)
        insidePanel.isOpaque = false
        insidePanel.cursor = Cursor(Cursor.HAND_CURSOR)

        add(insidePanel, BorderLayout.WEST)
        putClientProperty(FileEditorManager.SEPARATOR_DISABLED, true)
        isOpaque = false
        this.addMouseListener(object : MouseListener {
            override fun mouseClicked(e: MouseEvent?) {
                editor.scrollingModel.scrollTo(LogicalPosition(line, 0), ScrollType.RELATIVE);
            }

            override fun mousePressed(e: MouseEvent?) {}

            override fun mouseReleased(e: MouseEvent?) {}

            override fun mouseEntered(e: MouseEvent?) {}

            override fun mouseExited(e: MouseEvent?) {}

        })
    }

    override fun getWeight(): Double {
        return 1.0
    }

}