package dev.bapps.architectural.ui.fakes

import dev.bapps.architectural.ui.Button

class FakeButton(val id: String) : Button {
    private val listeners : MutableSet<Button.OnClickListener> = hashSetOf()

    private var _isEnabled: Boolean = true
    override val isEnabled: Boolean
        get() = _isEnabled

    private var _text: String = ""
    override val text: String
        get() = _text

    override fun setText(text: String) {
        _text = text
    }

    override fun addOnClickListener(listener: Button.OnClickListener) {
        listeners += listener
    }

    override fun removeOnClickListener(listener: Button.OnClickListener) {
        listeners -= listener
    }

    override fun enable() {
        _isEnabled = true
    }

    override fun disable() {
        _isEnabled = false
    }

    override fun click() {
        if (!isEnabled) return
        listeners.forEach(Button.OnClickListener::onClick)
    }

    override fun cleanup() {
        listeners.clear()
    }
}
