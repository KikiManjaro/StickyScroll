package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parents

/**
 * Marshaller for C# language.
 * Supports classes, interfaces, structs, enums, namespaces, methods and properties.
 * Uses class-name heuristics to avoid hard compile-time dependency on C# plugin (Rider).
 */
class CSharpParentMarshaller : PsiParentMarshaller, DefaultTextRangeMarshaller() {
    override fun getParents(psiElement: PsiElement?): Sequence<PsiElement>? {
        return psiElement?.parents(false)?.filter { element ->
            runCatching {
                val className = element.javaClass.name
                val simpleName = element.javaClass.simpleName
                val elementType = runCatching { element.node?.elementType.toString() }.getOrDefault("")
                className.contains("CSharp") ||
                className.contains("C#") ||
                className.contains("CSharpClass") ||
                className.contains("CSharpMethod") ||
                className.contains("CSharpNamespace") ||
                className.contains("CSharpInterface") ||
                simpleName.contains("CSharp") ||
                simpleName.contains("CSharpClass") ||
                simpleName.contains("CSharpMethod") ||
                elementType.contains("CLASS", ignoreCase = true) ||
                elementType.contains("INTERFACE", ignoreCase = true) ||
                elementType.contains("NAMESPACE", ignoreCase = true) ||
                elementType.contains("METHOD", ignoreCase = true) ||
                elementType.contains("STRUCT", ignoreCase = true) ||
                elementType.contains("ENUM", ignoreCase = true)
            }.getOrDefault(false)
        }
    }
}
