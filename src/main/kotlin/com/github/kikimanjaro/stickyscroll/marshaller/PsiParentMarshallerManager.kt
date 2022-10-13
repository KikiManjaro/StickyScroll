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
            if (language == Language.findLanguageByID("kotlin")) {
                return kotlinParentMarshaller
            } else if (language == Language.findLanguageByID("JSON")) {
                return jsonParentMarshaller
            } else if (language?.baseLanguage == Language.findLanguageByID("XML")) {
                return xmlParentMarshaller
            } else if (language == Language.findLanguageByID("Python")) {
                return pythonParentMarshaller
            } else {
                return defaultParentMarshaller
            }
        }
    }
}