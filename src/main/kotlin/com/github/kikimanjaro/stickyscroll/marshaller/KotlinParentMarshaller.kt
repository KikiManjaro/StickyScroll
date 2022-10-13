package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parents
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtDeclarationWithBody

class KotlinParentMarshaller : PsiParentMarshaller, DefaultTextRangeMarshaller() {
    override fun getParents(psiElement: PsiElement?): Sequence<PsiElement>? {
        return kotlin.runCatching {
            psiElement?.parents(false)
                ?.filter { element -> element is KtDeclarationWithBody || element is KtClassOrObject }
        }.getOrNull()
    }
}