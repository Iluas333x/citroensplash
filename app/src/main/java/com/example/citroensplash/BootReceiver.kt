package com.example.citroensplash

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Fired once by the system right after boot. Does the absolute minimum
 * amount of work — a SharedPreferences read and, if enabled, starting one
 * Activity — then returns immediately. There is no service, no polling,
 * and nothing else running afterward, so this has no ongoing footprint
 * and does not measurably affect boot time.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            "android.intent.action.QUICKBOOT_POWERON",
            "com.htc.intent.action.QUICKBOOT_POWERON" -> {
                if (!Prefs(context).isEnabled) return

                val splashIntent = Intent(context, SplashActivity::class.java).apply {
                    addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_NO_HISTORY or
                            Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                    )
                }

                runCatching { context.startActivity(splashIntent) }
                // If this fails on a particular head unit's ROM (some
                // heavily customized vendor builds add extra restrictions
                // on activities started from BOOT_COMPLETED), we deliberately
                // fail silently rather than retry or crash — a missed splash
                // screen should never block the user from reaching their
                // normal launcher.
            }
        }
    }
}
