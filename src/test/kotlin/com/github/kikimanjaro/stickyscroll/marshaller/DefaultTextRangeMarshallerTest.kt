package com.github.kikimanjaro.stickyscroll.marshaller

import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class DefaultTextRangeMarshallerTest : BasePlatformTestCase() {

    fun testJavadocIsNotPinnedInJavaHeader() {
        val headerWithJavadoc = pinnedClassHeaderOf(
            """
            /**
             * A long Javadoc block that must never be pinned.
             * @param none
             */
            public class Foo {
                public void bar() {
                    int x = 1;
                }
            }
            """.trimIndent()
        )
        val headerWithoutJavadoc = pinnedClassHeaderOf(
            """
            public class Foo {
                public void bar() {
                    int x = 1;
                }
            }
            """.trimIndent()
        )

        assertFalse(
            "Javadoc must not be part of the pinned header but got '$headerWithJavadoc'",
            headerWithJavadoc.contains("Javadoc")
        )
        assertEquals(headerWithoutJavadoc, headerWithJavadoc)
    }

    /**
     * Returns the text of the sticky fragment computed for the class declaration of the given Java [code].
     */
    private fun pinnedClassHeaderOf(code: String): String {
        val file: PsiFile = myFixture.configureByText("Foo.java", code)
        val document = PsiDocumentManager.getInstance(project).getDocument(file)!!
        val elementInsideMethod = file.findElementAt(code.indexOf("int x"))!!
        val marshaller = DefaultParentMarshaller()
        // The marshaller returns the enclosing scopes closest first: the method, then the class
        val psiClass = marshaller.getParents(elementInsideMethod)!!.last()
        val range = marshaller.getTextRangeAndStartLine(psiClass, document).first
        return document.text.substring(range.startOffset, range.endOffset)
    }
}
