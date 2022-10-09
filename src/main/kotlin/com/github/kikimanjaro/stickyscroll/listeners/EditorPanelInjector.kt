package com.github.kikimanjaro.stickyscroll.listeners

import com.github.kikimanjaro.stickyscroll.services.StickyPanelManager
import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class EditorPanelInjector(private val project: Project) : FileEditorManagerListener, LafManagerListener {
    private val logger = Logger.getInstance(javaClass)
    private var isFirstSetup = true

    init {
        ApplicationManager.getApplication().messageBus.connect(project).let {
            it.subscribe(LafManagerListener.TOPIC, this)
        }
    }
    override fun fileOpened(fem: FileEditorManager, virtualFile: VirtualFile) {
        for (textEditor in fem.getEditors(virtualFile).filterIsInstance<TextEditor>()) {
            val editor = textEditor.editor as? EditorImpl
            StickyPanelManager(project, editor!!, fem, textEditor)
        }
    }

    /** LafManagerListener */
    override fun lookAndFeelChanged(source: LafManager) = if (isFirstSetup) isFirstSetup = false else { //TODO: see if needed to reapply top component }
    }
}