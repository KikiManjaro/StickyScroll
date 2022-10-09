package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.parents
import com.intellij.psi.xml.XmlTag
import net.n3.nanoxml.XMLElement

class XMLParentMarshaller : PsiParentMarshaller {
    override fun getParents(element: PsiElement?): Sequence<PsiElement>? {
        return element?.parents(false)?.filter { element -> element is XmlTag }
    }
}