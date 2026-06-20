package dev.bapps.creational

class SingletonExampleType private constructor() {

    val innerValue = "some_value"

    companion object {
        private var instance: SingletonExampleType? = null
        fun get(): SingletonExampleType {
            if (instance == null)
                instance = SingletonExampleType()

            return requireNotNull(instance)
        }
    }
}