package com.github.kikimanjaro.stickyscroll

import com.github.kikimanjaro.stickyscroll.listeners.ScrollListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer

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

    fun addPanel(text: String, line: Int) {
//        if (panels.isEmpty() || panels.last().text != text) {
        this.panels.add(StickyPanel(editor, text, line))
//        }
//        refreshPanels()
    }

    fun clearPanelList() {
        removeTopLabels()
        this.panels.clear()
    }

    fun addTopLabels() {
        for (label in panels) {
            fem.addTopComponent(textEditor, label)
            label.parent
        }
    }

    fun removeTopLabels() {
        for (panel in panels) {
            if (panel.parent != null) {
                fem.removeTopComponent(textEditor, panel)
            }
        }
    }

}