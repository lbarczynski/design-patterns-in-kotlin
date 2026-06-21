package dev.bapps.creational

/**
 * Singleton implementation demonstrating the classic Java-style Double-Checked Locking pattern.
 * https://en.wikipedia.org/wiki/Singleton_pattern
 *
 * NOTE: In Kotlin, the idiomatic and highly recommended way to create a Singleton
 * is by simply using the `object` keyword (e.g., `object MySingleton { }`),
 * which is inherently thread-safe and lazily initialized by the JVM.
 * This manual implementation is provided purely for educational purposes.
 */
class Singleton private constructor() {
    val innerValue = "some_value"

    companion object {
        @Volatile
        private var instance: Singleton? = null

        fun getInstance(): Singleton {
            return instance ?: synchronized(this) {
                instance ?: Singleton().also { instance = it }
            }
        }
    }
}
