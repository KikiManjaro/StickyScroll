package com.github.kikimanjaro.stickyscroll

import com.github.kikimanjaro.stickyscroll.listeners.ScrollListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.ui.LightweightHint
import java.awt.Color
import javax.swing.BorderFactory

class StickyPanelManager(
    val project: Project,
    val editor: EditorImpl,
    val fem: FileEditorManager,
    val textEditor: TextEditor
) : Disposable {

    val panels = ArrayList<StickyPanel>()

    init {
        Disposer.register(editor.disposable, this)
        Disposer.register(this, ScrollListener(this))
    }

    override fun dispose() {
        removeTopLabels()
    }

    fun refreshPanels() {
        removeTopLabels()
        addTopLabels()
    }

    fun addPanel(hint: LightweightHint, line: Int) {
        for (panel in panels) {
            if (panel.line == line) {
                return
            }
        }
        val sticky = StickyPanel(editor, hint, line)
        this.panels.add(sticky)
    }

    fun clearPanelList() {
        removeTopLabels()
        this.panels.clear()
    }

    fun addTopLabels() {
        panels.forEachIndexed { index, sticky ->
            fem.addTopComponent(textEditor, sticky.hint.component)
            if (index == panels.size - 1) {
                sticky.hint.component.border = BorderFactory.createMatteBorder(0, 0, 3, 0, Color(30,30,30,90))
            }
        }
    }

    fun removeTopLabels() {
        for (panel in panels) {
            if (panel.hint.component != null) {
                fem.removeTopComponent(textEditor, panel.hint.component)
            }
        }
    }

}