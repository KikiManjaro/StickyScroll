package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parents

/**
 * Marshaller for Rust language.
 * Supports structs, enums, traits, impl blocks, functions, methods and modules.
 * Uses class-name heuristics to avoid hard compile-time dependency on intellij-rust plugin (org.rust.lang).
 */
class RustParentMarshaller : PsiParentMarshaller, DefaultTextRangeMarshaller() {
    override fun getParents(psiElement: PsiElement?): Sequence<PsiElement>? {
        return psiElement?.parents(false)?.filter { element ->
            runCatching {
                val className = element.javaClass.name
                val simpleName = element.javaClass.simpleName
                val elementType = runCatching { element.node?.elementType.toString() }.getOrDefault("")
                className.contains("RsStruct") ||
                className.contains("RsEnum") ||
                className.contains("RsTrait") ||
                className.contains("RsImpl") ||
                className.contains("RsFunction") ||
                className.contains("RsMod") ||
                className.contains("Rust") ||
                simpleName.contains("RsStruct") ||
                simpleName.contains("RsFunction") ||
                simpleName.contains("RsImpl") ||
                simpleName.contains("RsMod") ||
                elementType.contains("STRUCT", ignoreCase = true) ||
                elementType.contains("ENUM", ignoreCase = true) ||
                elementType.contains("TRAIT", ignoreCase = true) ||
                elementType.contains("IMPL", ignoreCase = true) ||
                elementType.contains("FUNCTION", ignoreCase = true) ||
                elementType.contains("MOD", ignoreCase = true)
            }.getOrDefault(false)
        }
    }
}
