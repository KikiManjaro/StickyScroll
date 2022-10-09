package com.github.kikimanjaro.stickyscroll.marshaller


import com.intellij.json.psi.JsonContainer
import com.intellij.json.psi.JsonObject
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parents

class JsonParentMarshaller : PsiParentMarshaller {
    override fun getParents(element: PsiElement?): Sequence<PsiElement>? {
        return element?.parents(false)?.filter { element -> element is JsonContainer }
    }
}