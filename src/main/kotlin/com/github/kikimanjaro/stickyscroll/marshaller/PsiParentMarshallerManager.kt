package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.lang.Language
class PsiParentMarshallerManager {
    companion object {
        private val defaultParentMarshaller = DefaultParentMarshaller()
        private val kotlinParentMarshaller = KotlinParentMarshaller()
        private val jsonParentMarshaller = JsonParentMarshaller()
        private val xmlParentMarshaller = XMLParentMarshaller()
        private val pythonParentMarshaller = PythonParentMarshaller()

        // Cached language lookups to avoid repeated findLanguageByID calls
        private val kotlinLanguage by lazy { Language.findLanguageByID("kotlin") }
        private val jsonLanguage by lazy { Language.findLanguageByID("JSON") }
        private val xmlLanguage by lazy { Language.findLanguageByID("XML") }
        private val pythonLanguage by lazy { Language.findLanguageByID("Python") }

        fun getParentMarshaller(language: Language?): PsiParentMarshaller? {
            if (language == null) return defaultParentMarshaller
            return when {
                language == kotlinLanguage -> kotlinParentMarshaller
                language == jsonLanguage -> jsonParentMarshaller
                language.baseLanguage == xmlLanguage -> xmlParentMarshaller
                language == pythonLanguage -> pythonParentMarshaller
                else -> defaultParentMarshaller
            }
        }
    }
}
