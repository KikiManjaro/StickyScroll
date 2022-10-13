package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.parents

class DefaultParentMarshaller : PsiParentMarshaller, DefaultTextRangeMarshaller() {
    override fun getParents(psiElement: PsiElement?): Sequence<PsiElement>? {
        return kotlin.runCatching {
            psiElement?.parents(false)?.filter { element -> element is PsiClass || element is PsiMethod }
        }.getOrNull()
    }
}