package dev.bapps.creational

import kotlin.test.Test
import kotlin.test.assertSame

class SingletonPatternTest {

    @Test
    fun `GIVEN initialized instance WHEN get THEN should return the same instance`() {
        val first = SingletonExampleType.get()
        val second = SingletonExampleType.get()
        assertSame(first.innerValue, second.innerValue)
    }
}
