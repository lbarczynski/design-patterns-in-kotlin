package dev.bapps.creational

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame

class MultitonPatternTest {

    @Test
    fun `GIVEN multiton WHEN getInstance called THEN should return unique instance per contextId`() {
        val firstA = Multiton.getInstance("A")
        val secondA = Multiton.getInstance("A")
        val firstB = Multiton.getInstance("B")

        assertSame(firstA, secondA)
        assertNotSame(firstA, firstB)
        assertEquals("A", firstA.contextId)
        assertEquals("B", firstB.contextId)
    }
}
