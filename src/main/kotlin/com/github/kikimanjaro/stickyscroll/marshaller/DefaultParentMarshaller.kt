package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parents

class DefaultParentMarshaller : PsiParentMarshaller, DefaultTextRangeMarshaller() {
    override fun getParents(psiElement: PsiElement?): Sequence<PsiElement>? {
        return try {
            psiElement?.parents(false)?.filter { element ->
                try {
                    // Use reflection-safe check to avoid NoClassDefFoundError when com.intellij.java is absent (e.g. WebStorm)
                    isJavaClassOrMethod(element)
                } catch (t: Throwable) {
                    false
                }
            }
        } catch (t: Throwable) {
            null
        }
    }

    private fun isJavaClassOrMethod(element: PsiElement): Boolean {
        // Check via class names to avoid hard linkage failure at class-load time in non-Java IDEs
        // Fallback to instanceof when class is available
        return try {
            val psiClass = Class.forName("com.intellij.psi.PsiClass")
            val psiMethod = Class.forName("com.intellij.psi.PsiMethod")
            psiClass.isInstance(element) || psiMethod.isInstance(element)
        } catch (e: ClassNotFoundException) {
            false
        } catch (t: Throwable) {
            // Last resort: try direct instanceof if reflection fails for other reasons
            try {
                @Suppress("USELESS_IS_CHECK")
                element.javaClass.name == "com.intellij.psi.impl.source.PsiClassImpl" ||
                element.javaClass.name == "com.intellij.psi.impl.source.PsiMethodImpl" ||
                element.toString().contains("PsiClass") || element.toString().contains("PsiMethod")
            } catch (_: Throwable) { false }
        }
    }
}
