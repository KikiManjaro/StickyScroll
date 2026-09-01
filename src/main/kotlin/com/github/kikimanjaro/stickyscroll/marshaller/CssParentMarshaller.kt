package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parents

/**
 * Marshaller for CSS language.
 * Supports rulesets, at-rules (e.g. @media, @supports, @keyframes) and style blocks.
 * Uses class-name heuristics to avoid hard compile-time dependency on CSS plugin.
 */
class CssParentMarshaller : PsiParentMarshaller, DefaultTextRangeMarshaller() {
    override fun getParents(psiElement: PsiElement?): Sequence<PsiElement>? {
        return psiElement?.parents(false)?.filter { element ->
            runCatching {
                val className = element.javaClass.name
                val simpleName = element.javaClass.simpleName
                val elementType = runCatching { element.node?.elementType.toString() }.getOrDefault("")
                className.contains("CssRuleset") ||
                className.contains("CssAtRule") ||
                className.contains("CssBlock") ||
                className.contains("CssMedia") ||
                className.contains("CssDeclaration") ||
                simpleName.contains("CssRuleset") ||
                simpleName.contains("CssAtRule") ||
                simpleName.contains("CssBlock") ||
                elementType.contains("RULESET", ignoreCase = true) ||
                elementType.contains("AT_RULE", ignoreCase = true) ||
                elementType.contains("MEDIA", ignoreCase = true) ||
                elementType.contains("BLOCK", ignoreCase = true) ||
                elementType.contains("RULES", ignoreCase = true)
            }.getOrDefault(false)
        }
    }
}
