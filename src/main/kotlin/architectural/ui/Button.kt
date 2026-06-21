package dev.bapps.architectural.ui

interface Button {
    val isEnabled: Boolean
    val text: String
    fun setText(text: String)
    fun enable()
    fun disable()
    fun click()
    fun addOnClickListener(listener: OnClickListener)
    fun removeOnClickListener(listener: OnClickListener)
    fun cleanup()

    fun interface OnClickListener {
        fun onClick()
    }
}
