package com.lifeosai.app.data.cache

import javax.inject.Inject
import javax.inject.Singleton
import java.util.concurrent.ConcurrentHashMap

@Singleton
class MemoryCache @Inject constructor() {
    private val cache = ConcurrentHashMap<String, Any>()

    fun <T : Any> put(key: String, value: T) {
        cache[key] = value
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(key: String): T? {
        return cache[key] as? T
    }

    fun remove(key: String) {
        cache.remove(key)
    }

    fun clear() {
        cache.clear()
    }
}
