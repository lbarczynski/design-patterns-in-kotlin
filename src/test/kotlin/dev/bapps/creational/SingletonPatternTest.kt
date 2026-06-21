package dev.bapps.creational

import kotlin.test.Test
import kotlin.test.assertSame

class SingletonPatternTest {

    @Test
    fun `GIVEN singleton WHEN getInstance called twice THEN should return the same instance`() {
        val first = Singleton.getInstance()
        val second = Singleton.getInstance()
        
        assertSame(first, second)
        assertSame(first.innerValue, second.innerValue)
    }
}
