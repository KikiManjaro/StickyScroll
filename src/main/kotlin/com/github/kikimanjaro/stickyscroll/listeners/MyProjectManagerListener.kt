package com.github.kikimanjaro.stickyscroll.listeners

import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener

internal class MyProjectManagerListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        project.messageBus.connect()
            .subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, EditorPanelInjector(project))
    }
}
