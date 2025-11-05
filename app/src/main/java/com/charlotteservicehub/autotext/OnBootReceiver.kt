package com.charlotteservicehub.autotext

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast

class OnBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Just a nudge: after boot, remind if notification access is off
        val enabled = Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        )?.contains(context.packageName) == true

        if (!enabled) {
            Toast.makeText(context, "Enable Notification Access for AutoText", Toast.LENGTH_LONG).show()
        }
    }
}
