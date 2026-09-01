package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.lang.Language

class PsiParentMarshallerManager {
    companion object {
        private val defaultParentMarshaller = DefaultParentMarshaller()
        private val kotlinParentMarshaller = KotlinParentMarshaller()
        private val jsonParentMarshaller = JsonParentMarshaller()
        private val xmlParentMarshaller = XMLParentMarshaller()
        private val pythonParentMarshaller = PythonParentMarshaller()
        fun getParentMarshaller(language: Language?): PsiParentMarshaller? {
            if (language == null) return defaultParentMarshaller
            // Kotlin
            if (language.id.equals("kotlin", ignoreCase = true)) return kotlinParentMarshaller
            // JSON
            if (language.id.equals("JSON", ignoreCase = true)) return jsonParentMarshaller
            // XML-based (html, xml, etc.)
            if (language.baseLanguage?.id.equals("XML", ignoreCase = true)) return xmlParentMarshaller
            // Python (covers both Python and Pythonid legacy)
            if (language.id.equals("Python", ignoreCase = true) || language.id.equals("Pythonid", ignoreCase = true)) {
                return pythonParentMarshaller
            }
            return defaultParentMarshaller
        }
    }
}