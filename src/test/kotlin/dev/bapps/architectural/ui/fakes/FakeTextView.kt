package dev.bapps.architectural.ui.fakes

import dev.bapps.architectural.ui.TextView

class FakeTextView(val id: String) : TextView {
    private var _text: String = ""
    override val text: String
        get() = _text

    override fun setText(text: String) {
        _text = text
    }
}
