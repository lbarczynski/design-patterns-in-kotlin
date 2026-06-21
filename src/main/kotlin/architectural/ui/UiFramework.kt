package dev.bapps.architectural.ui

interface UiFramework {
    fun findButtonById(id: String): Button
    fun findTextViewById(id: String): TextView
}
