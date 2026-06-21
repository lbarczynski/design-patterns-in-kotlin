package dev.bapps.creational

import java.util.concurrent.ConcurrentHashMap

// https://en.wikipedia.org/wiki/Multiton_pattern
class Multiton private constructor(val contextId: String) {

    companion object {
        private val instances = ConcurrentHashMap<String, Multiton>()

        fun getInstance(contextId: String): Multiton {
            return instances.computeIfAbsent(contextId) { key ->
                Multiton(key)
            }
        }
    }
}
