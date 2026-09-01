package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parents

/**
 * Marshaller for JavaScript language.
 * Handles classes, functions, methods, and object literals that define scopes.
 * Uses runtime class-name heuristics to avoid mandatory compile-time dependency on JavaScript plugin.
 */
class JavaScriptParentMarshaller : PsiParentMarshaller, DefaultTextRangeMarshaller() {
    override fun getParents(psiElement: PsiElement?): Sequence<PsiElement>? {
        return psiElement?.parents(false)?.filter { element ->
            runCatching {
                val className = element.javaClass.name
                val simpleName = element.javaClass.simpleName
                val elementType = runCatching { element.node?.elementType.toString() }.getOrDefault("")
                className.contains("JSClass") ||
                className.contains("JSFunction") ||
                simpleName.contains("JSClass") ||
                simpleName.contains("JSFunction") ||
                elementType.contains("CLASS", ignoreCase = true) ||
                elementType.contains("FUNCTION", ignoreCase = true) ||
                elementType.contains("METHOD", ignoreCase = true)
            }.getOrDefault(false)
        }
    }
}
