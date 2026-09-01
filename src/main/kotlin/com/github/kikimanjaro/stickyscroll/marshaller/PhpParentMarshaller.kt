package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parents

/**
 * Marshaller for PHP language.
 * Supports classes, interfaces, traits, functions, methods, and namespaces.
 * Uses class-name heuristics to avoid hard compile dependency on PHP plugin at build time.
 */
class PhpParentMarshaller : PsiParentMarshaller, DefaultTextRangeMarshaller() {
    override fun getParents(psiElement: PsiElement?): Sequence<PsiElement>? {
        return psiElement?.parents(false)?.filter { element ->
            runCatching {
                val className = element.javaClass.name
                val simpleName = element.javaClass.simpleName
                val elementType = runCatching { element.node?.elementType.toString() }.getOrDefault("")
                className.contains("PhpClass") ||
                className.contains("PhpFunction") ||
                className.contains("PhpMethod") ||
                className.contains("PhpTrait") ||
                className.contains("PhpInterface") ||
                className.contains("PhpNamespace") ||
                simpleName.contains("PhpClass") ||
                simpleName.contains("PhpFunction") ||
                simpleName.contains("PhpMethod") ||
                elementType.contains("CLASS", ignoreCase = true) ||
                elementType.contains("FUNCTION", ignoreCase = true) ||
                elementType.contains("METHOD", ignoreCase = true) ||
                elementType.contains("NAMESPACE", ignoreCase = true)
            }.getOrDefault(false)
        }
    }
}
