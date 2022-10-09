package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.psi.PsiElement

interface PsiParentMarshaller {

    fun getParents(element: PsiElement?): Sequence<PsiElement>?
}