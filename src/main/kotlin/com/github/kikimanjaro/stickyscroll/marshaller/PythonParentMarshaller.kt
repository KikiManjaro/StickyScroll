package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parents
import com.jetbrains.python.codeInsight.controlflow.ScopeOwner
import com.jetbrains.python.psi.PyFile

class PythonParentMarshaller : PsiParentMarshaller, DefaultTextRangeMarshaller() {
    override fun getParents(psiElement: PsiElement?): Sequence<PsiElement>? {
        return psiElement?.parents(false)?.filter { element ->
            runCatching { element is ScopeOwner && element !is PyFile }.getOrDefault(false)
        }
    }

}