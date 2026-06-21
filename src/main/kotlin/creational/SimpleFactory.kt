package dev.bapps.creational

enum class OsType {
    Windows, Linux, MacOS
}

interface UiButton
class WindowsUiButton : UiButton
class LinuxButton : UiButton
class MacOsButton : UiButton

object ButtonFactory {
    fun createButton(osType: OsType): UiButton {
        return when (osType) {
            OsType.Windows -> WindowsUiButton()
            OsType.Linux -> LinuxButton()
            OsType.MacOS -> MacOsButton()
        }
    }
}
