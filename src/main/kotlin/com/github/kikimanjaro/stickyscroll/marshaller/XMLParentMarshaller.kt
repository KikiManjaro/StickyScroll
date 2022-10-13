package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parents
import com.intellij.psi.xml.XmlTag

class XMLParentMarshaller : PsiParentMarshaller, DefaultTextRangeMarshaller() {
    override fun getParents(psiElement: PsiElement?): Sequence<PsiElement>? {
        return psiElement?.parents(false)?.filter { element -> element is XmlTag }
    }
}