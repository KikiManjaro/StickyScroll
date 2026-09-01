package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parents

class PythonParentMarshaller : PsiParentMarshaller, DefaultTextRangeMarshaller() {
    override fun getParents(psiElement: PsiElement?): Sequence<PsiElement>? {
        return try {
            psiElement?.parents(false)?.filter { element ->
                try {
                    isPythonScope(element)
                } catch (t: Throwable) {
                    false
                }
            }
        } catch (t: Throwable) {
            null
        }
    }

    private fun isPythonScope(element: PsiElement): Boolean {
        return try {
            val scopeOwner = Class.forName("com.jetbrains.python.codeInsight.controlflow.ScopeOwner")
            val pyFile = Class.forName("com.jetbrains.python.psi.PyFile")
            scopeOwner.isInstance(element) && !pyFile.isInstance(element)
        } catch (e: ClassNotFoundException) {
            false
        } catch (t: Throwable) {
            false
        }
    }
}
