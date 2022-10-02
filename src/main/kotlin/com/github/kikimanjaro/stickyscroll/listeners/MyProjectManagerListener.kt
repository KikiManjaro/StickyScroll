package com.github.kikimanjaro.stickyscroll.listeners

import com.github.kikimanjaro.stickyscroll.EditorPanelInjector
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.github.kikimanjaro.stickyscroll.services.MyProjectService
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parents
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.Document

internal class MyProjectManagerListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        project.service<MyProjectService>()
        project.messageBus.connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, EditorPanelInjector(project))

//        val editor = FileEditorManager.getInstance(project).selectedTextEditor
//        val currentDoc = editor?.document
//        if(currentDoc != null){
//            val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(currentDoc)
//            val currentElement = psiFile?.findElementAt(editor.caretModel.offset)
//            println(currentElement?.parents(false))
//        }
//
//        currentDoc?.addDocumentListener(object: DocumentListener, com.intellij.openapi.editor.event.DocumentListener {
//            override fun insertUpdate(e: DocumentEvent?) {
//                    val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(currentDoc)
//                    val currentElement = psiFile?.findElementAt(editor.caretModel.offset)
//                    println(currentElement?.parents(false))
//            }
//
//            override fun removeUpdate(e: DocumentEvent?) {
//                    val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(currentDoc)
//                    val currentElement = psiFile?.findElementAt(editor.caretModel.offset)
//                    println(currentElement?.parents(false))
//            }
//
//            override fun changedUpdate(e: DocumentEvent?) {
//                    val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(currentDoc)
//                    val currentElement = psiFile?.findElementAt(editor.caretModel.offset)
//                    println(currentElement?.parents(false))
//            }
//        })
//
//
//        EditorFactory.getInstance().eventMulticaster.addDocumentListener(object : DocumentListener,
//            com.intellij.openapi.editor.event.DocumentListener {
//            override fun insertUpdate(e: DocumentEvent?) {
//                val editor = FileEditorManager.getInstance(project).selectedTextEditor
//                val currentDoc = editor?.document
//                if(currentDoc != null){
//                    val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(currentDoc)
//                    val currentElement = psiFile?.findElementAt(editor.caretModel.offset)
//                    println(currentElement?.parents(false))
//                }
//            }
//
//            override fun removeUpdate(e: DocumentEvent?) {
//                val editor = FileEditorManager.getInstance(project).selectedTextEditor
//                val currentDoc = editor?.document
//                if(currentDoc != null){
//                    val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(currentDoc)
//                    val currentElement = psiFile?.findElementAt(editor.caretModel.offset)
//                    println(currentElement?.parents(false))
//                }
//            }
//
//            override fun changedUpdate(e: DocumentEvent?) {
//                val editor = FileEditorManager.getInstance(project).selectedTextEditor
//                val currentDoc = editor?.document
//                if(currentDoc != null){
//                    val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(currentDoc)
//                    val currentElement = psiFile?.findElementAt(editor.caretModel.offset)
//                    println(currentElement?.parents(false))
//                }
//            }
//
//        }) { }
    }
}
