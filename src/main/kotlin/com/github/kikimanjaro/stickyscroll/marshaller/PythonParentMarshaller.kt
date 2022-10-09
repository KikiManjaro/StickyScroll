package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parents
import org.jetbrains.kotlin.nj2k.inference.common.getLabel

class PythonParentMarshaller : PsiParentMarshaller {
    override fun getParents(element: PsiElement?): Sequence<PsiElement>? {
        return element?.parents(false)?.filter { element -> element.elementType?.debugName == "METHOD" }
    }
}