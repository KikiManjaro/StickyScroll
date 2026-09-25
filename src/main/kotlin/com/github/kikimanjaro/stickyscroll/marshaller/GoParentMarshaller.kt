package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parents

/**
 * Marshaller for Go language.
 * Supports functions, methods, type declarations, structs and interfaces.
 * Uses class-name heuristics to avoid hard compile-time dependency on the Go plugin (org.jetbrains.plugins.go).
 */
class GoParentMarshaller : PsiParentMarshaller, DefaultTextRangeMarshaller() {
    override fun getParents(psiElement: PsiElement?): Sequence<PsiElement>? {
        return psiElement?.parents(false)?.filter { element ->
            runCatching {
                val className = element.javaClass.name
                val simpleName = element.javaClass.simpleName
                className.contains("GoFunctionDeclaration") ||
                className.contains("GoMethodDeclaration") ||
                className.contains("GoTypeDeclaration") ||
                className.contains("GoTypeSpec") ||
                className.contains("GoStructType") ||
                className.contains("GoInterfaceType") ||
                simpleName.contains("GoFunctionDeclaration") ||
                simpleName.contains("GoMethodDeclaration") ||
                simpleName.contains("GoTypeDeclaration") ||
                simpleName.contains("GoTypeSpec") ||
                simpleName.contains("GoStructType") ||
                simpleName.contains("GoInterfaceType")
            }.getOrDefault(false)
        }
    }
}
