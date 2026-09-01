package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parents

/**
 * Marshaller for TypeScript language.
 * Filters parent PSI elements that represent scopes: classes, interfaces, functions, methods.
 * Uses class-name heuristics to avoid hard compile-time dependency on JavaScript plugin.
 */
class TypeScriptParentMarshaller : PsiParentMarshaller, DefaultTextRangeMarshaller() {
    override fun getParents(psiElement: PsiElement?): Sequence<PsiElement>? {
        return psiElement?.parents(false)?.filter { element ->
            runCatching {
                val className = element.javaClass.name
                val simpleName = element.javaClass.simpleName
                val elementType = runCatching { element.node?.elementType.toString() }.getOrDefault("")
                className.contains("JSClass") ||
                className.contains("JSFunction") ||
                className.contains("TypeScript") ||
                simpleName.contains("JSClass") ||
                simpleName.contains("JSFunction") ||
                elementType.contains("CLASS", ignoreCase = true) ||
                elementType.contains("FUNCTION", ignoreCase = true) ||
                elementType.contains("METHOD", ignoreCase = true) ||
                elementType.contains("INTERFACE", ignoreCase = true)
            }.getOrDefault(false)
        }
    }
}
