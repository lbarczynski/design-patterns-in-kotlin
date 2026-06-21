package dev.bapps.creational

interface Button {
    fun render(): String
    fun onClick()
}

class WinApiButton : Button {
    override fun render(): String = "Windows Button"
    override fun onClick() { println("Windows click") }
}

class KdeButton : Button {
    override fun render(): String = "KDE Button"
    override fun onClick() { println("KDE button click") }
}

class LiquidGlassButton : Button {
    override fun render(): String = "MacOS Button"
    override fun onClick() { println("MacOS button click") }
}

abstract class Dialog {
    /**
     * createButton() is our "factory method"
     *
     * The whole concept is to follow SOLID rules and allow abstract
     * type-specified object creation thanks to the inheritance.
     * But remember, composition over inheritance!
     */
    abstract fun createButton(): Button

    fun renderWindow(): String {
        val button = createButton()
        button.onClick()
        return "Dialog rendering: " + button.render()
    }
}

class WindowsDialog : Dialog() {
    override fun createButton(): Button = WinApiButton()
}

class LinuxDialog : Dialog() {
    override fun createButton(): Button = KdeButton()
}

class MacOsDialog : Dialog() {
    override fun createButton(): Button = LiquidGlassButton()
}
