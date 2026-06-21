package dev.bapps.creational

import kotlin.test.Test
import kotlin.test.assertIs

class SimpleFactoryTest {

    @Test
    fun `GIVEN Windows osType WHEN createButton THEN should return WindowsUiButton`() {
        val button = ButtonFactory.createButton(OsType.Windows)
        assertIs<WindowsUiButton>(button)
    }

    @Test
    fun `GIVEN Linux osType WHEN createButton THEN should return LinuxButton`() {
        val button = ButtonFactory.createButton(OsType.Linux)
        assertIs<LinuxButton>(button)
    }
    
    @Test
    fun `GIVEN MacOS osType WHEN createButton THEN should return MacOsButton`() {
        val button = ButtonFactory.createButton(OsType.MacOS)
        assertIs<MacOsButton>(button)
    }
}
