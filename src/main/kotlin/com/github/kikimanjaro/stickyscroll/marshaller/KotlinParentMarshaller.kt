package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parents

class KotlinParentMarshaller : PsiParentMarshaller, DefaultTextRangeMarshaller() {
    override fun getParents(psiElement: PsiElement?): Sequence<PsiElement>? {
        return try {
            psiElement?.parents(false)?.filter { element ->
                try {
                    isKotlinClassOrFunction(element)
                } catch (t: Throwable) {
                    false
                }
            }
        } catch (t: Throwable) {
            null
        }
    }

    private fun isKotlinClassOrFunction(element: PsiElement): Boolean {
        return try {
            val ktClass = Class.forName("org.jetbrains.kotlin.psi.KtClassOrObject")
            val ktFunc = Class.forName("org.jetbrains.kotlin.psi.KtDeclarationWithBody")
            ktClass.isInstance(element) || ktFunc.isInstance(element)
        } catch (e: ClassNotFoundException) {
            false
        } catch (t: Throwable) {
            false
        }
    }
}
