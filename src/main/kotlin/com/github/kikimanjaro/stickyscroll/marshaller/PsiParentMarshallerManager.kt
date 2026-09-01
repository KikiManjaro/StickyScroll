package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.lang.Language

class PsiParentMarshallerManager {
    companion object {
        private val defaultParentMarshaller by lazy { DefaultParentMarshaller() }
        private val kotlinParentMarshaller by lazy { try { KotlinParentMarshaller() } catch (t: Throwable) { null } }
        private val jsonParentMarshaller by lazy { try { JsonParentMarshaller() } catch (t: Throwable) { null } }
        private val xmlParentMarshaller by lazy { try { XMLParentMarshaller() } catch (t: Throwable) { null } }
        private val pythonParentMarshaller by lazy { try { PythonParentMarshaller() } catch (t: Throwable) { null } }

        fun getParentMarshaller(language: Language?): PsiParentMarshaller? {
            return try {
                when {
                    language == Language.findLanguageByID("kotlin") -> kotlinParentMarshaller
                    language == Language.findLanguageByID("JSON") -> jsonParentMarshaller
                    language?.baseLanguage == Language.findLanguageByID("XML") -> xmlParentMarshaller
                    language == Language.findLanguageByID("Python") -> pythonParentMarshaller
                    else -> defaultParentMarshaller
                }
            } catch (t: Throwable) {
                try { defaultParentMarshaller } catch (_: Throwable) { null }
            }
        }
    }
}
