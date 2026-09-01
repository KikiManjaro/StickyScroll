package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.lang.Language

class PsiParentMarshallerManager {
    companion object {
        private val defaultParentMarshaller = DefaultParentMarshaller()
        private val kotlinParentMarshaller = KotlinParentMarshaller()
        private val jsonParentMarshaller = JsonParentMarshaller()
        private val xmlParentMarshaller = XMLParentMarshaller()
        private val pythonParentMarshaller = PythonParentMarshaller()
        private val cSharpParentMarshaller = CSharpParentMarshaller()
        fun getParentMarshaller(language: Language?): PsiParentMarshaller? {
            if (language == Language.findLanguageByID("kotlin")) {
                return kotlinParentMarshaller
            } else if (language == Language.findLanguageByID("JSON")) {
                return jsonParentMarshaller
            } else if (language?.baseLanguage == Language.findLanguageByID("XML")) {
                return xmlParentMarshaller
            } else if (language == Language.findLanguageByID("Python")) {
                return pythonParentMarshaller
            } else if (language == Language.findLanguageByID("C#") || language == Language.findLanguageByID("CSharp")) {
                return cSharpParentMarshaller
            } else if (language?.id == "C#" || language?.id == "CSharp" || language?.id == "C#_") {
                return cSharpParentMarshaller
            } else {
                return defaultParentMarshaller
            }
        }
    }
}