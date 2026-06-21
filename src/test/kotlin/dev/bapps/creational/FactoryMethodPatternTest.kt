package dev.bapps.creational

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class FactoryMethodPatternTest {

    @Test
    fun `GIVEN WindowsDialog WHEN renderWindow THEN should use WinApiButton`() {
        // given
        val dialog: Dialog = WindowsDialog()
        
        // when
        val result = dialog.renderWindow()
        
        // then
        assertEquals("Dialog rendering: Windows Button", result)
        assertIs<WinApiButton>(dialog.createButton())
    }

    @Test
    fun `GIVEN LinuxDialog WHEN renderWindow THEN should use KdeButton`() {
        // given
        val dialog: Dialog = LinuxDialog()
        
        // when
        val result = dialog.renderWindow()
        
        // then
        assertEquals("Dialog rendering: KDE Button", result)
        assertIs<KdeButton>(dialog.createButton())
    }

    @Test
    fun `GIVEN MacOsDialog WHEN renderWindow THEN should use LiquidGlassButton`() {
        // given
        val dialog: Dialog = MacOsDialog()
        
        // when
        val result = dialog.renderWindow()
        
        // then
        assertEquals("Dialog rendering: MacOS Button", result)
        assertIs<LiquidGlassButton>(dialog.createButton())
    }
}
