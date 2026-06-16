package com.example.citroensplash

import android.content.Context

/**
 * Thin wrapper around SharedPreferences. No background threads, no disk
 * polling — reads/writes are tiny and synchronous, which is appropriate
 * for two booleans/ints read once at boot.
 */
class Prefs(context: Context) {

    private val sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var isEnabled: Boolean
        get() = sp.getBoolean(KEY_ENABLED, true)
        set(value) = sp.edit().putBoolean(KEY_ENABLED, value).apply()

    /** Splash duration in whole seconds, clamped to [MIN_DURATION_SECONDS, MAX_DURATION_SECONDS]. */
    var durationSeconds: Int
        get() = sp.getInt(KEY_DURATION, DEFAULT_DURATION_SECONDS)
            .coerceIn(MIN_DURATION_SECONDS, MAX_DURATION_SECONDS)
        set(value) = sp.edit()
            .putInt(KEY_DURATION, value.coerceIn(MIN_DURATION_SECONDS, MAX_DURATION_SECONDS))
            .apply()

    companion object {
        private const val PREFS_NAME = "splash_prefs"
        private const val KEY_ENABLED = "splash_enabled"
        private const val KEY_DURATION = "splash_duration_seconds"

        const val DEFAULT_DURATION_SECONDS = 4
        const val MIN_DURATION_SECONDS = 2
        const val MAX_DURATION_SECONDS = 10
    }
}
