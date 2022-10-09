package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.parents
import com.intellij.psi.util.parentsOfType

class DefaultParentMarshaller : PsiParentMarshaller{
    override fun getParents(element: PsiElement?): Sequence<PsiElement>? {
        return element?.parents(false)?.filter { element -> element is PsiClass || element is PsiMethod }
    }
}