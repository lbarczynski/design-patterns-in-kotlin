package dev.bapps.architectural.ui.fakes

import dev.bapps.architectural.ui.Button
import dev.bapps.architectural.ui.TextView
import dev.bapps.architectural.ui.UiFramework

class FakeUiFramework(vararg views: Pair<String, Any>) : UiFramework {
    private val viewMap = mapOf(*views)
    
    override fun findButtonById(id: String): Button {
        return viewMap[id] as? Button ?: throw IllegalArgumentException("Button $id not found")
    }

    override fun findTextViewById(id: String): TextView {
        return viewMap[id] as? TextView ?: throw IllegalArgumentException("TextView $id not found")
    }
}
