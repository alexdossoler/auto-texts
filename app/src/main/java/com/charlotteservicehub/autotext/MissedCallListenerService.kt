package com.charlotteservicehub.autotext

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.CallLog
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.telephony.SmsManager
import android.telephony.SubscriptionManager
import android.text.format.DateUtils
import android.util.Log
import java.util.Locale
import java.util.regex.Pattern

class MissedCallListenerService : NotificationListenerService() {

    // Simple per-number rate limiting to avoid spamming (15 minutes)
    private val prefs by lazy { getSharedPreferences("autotext_prefs", Context.MODE_PRIVATE) }
    private val MIN_INTERVAL_MS = 15 * 60 * 1000L

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // Dialer-related packages (varies by device/OEM)
        val pkg = sbn.packageName ?: return
        val dialerPkgs = setOf(
            "com.google.android.dialer",
            "com.android.server.telecom",
            "com.android.phone"
        )
        if (!dialerPkgs.contains(pkg)) return

        val extras = sbn.notification.extras
        val title = (extras?.getCharSequence("android.title")?.toString() ?: "")
        val text = (extras?.getCharSequence("android.text")?.toString() ?: "")

        // Handle common locales: English/Spanish
        val isMissed = title.contains("Missed call", true) ||
                title.contains("Llamada perdida", true) ||
                text.contains("Missed call", true) ||
                text.contains("Llamada perdida", true)

        if (!isMissed) return

        // Try to get number from the notification text first (sometimes present)
        val candidateFromText = extractPhoneFromText(title) ?: extractPhoneFromText(text)

        val number = candidateFromText ?: getLastMissedNumber(windowMs = 2 * 60 * 1000L)
        if (number.isNullOrBlank()) {
            Log.w("AutoText", "No number found for missed call")
            return
        }

        if (!shouldSendTo(number)) {
            Log.i("AutoText", "Skipping auto-text for $number (rate-limited or invalid)")
            return
        }

        val message = buildMessage()
        val sent = sendSms(number, message)
        if (sent) {
            markSent(number)
            Log.i("AutoText", "Auto-text sent to $number")
        } else {
            Log.e("AutoText", "Failed to send SMS to $number")
        }
    }

    private fun extractPhoneFromText(s: String?): String? {
        if (s.isNullOrBlank()) return null
        // Grab digits, allow +, -, spaces, parentheses
        val p = Pattern.compile("(\\+?\\d[\\d\\-\\s().]{6,}\\d)")
        val m = p.matcher(s)
        return if (m.find()) {
            m.group(1)?.let { PhoneUtils.normalize(it) }
        } else null
    }

    private fun getLastMissedNumber(windowMs: Long): String? {
        val uri: Uri = CallLog.Calls.CONTENT_URI
        val now = System.currentTimeMillis()
        val sel = "${CallLog.Calls.TYPE}=? AND ${CallLog.Calls.DATE}>=?"
        val args = arrayOf(CallLog.Calls.MISSED_TYPE.toString(), (now - windowMs).toString())
        val sort = "${CallLog.Calls.DATE} DESC"

        var c: Cursor? = null
        return try {
            c = contentResolver.query(uri, arrayOf(CallLog.Calls.NUMBER), sel, args, sort)
            if (c != null && c.moveToFirst()) {
                PhoneUtils.normalize(c.getString(0))
            } else null
        } catch (t: Throwable) {
            Log.e("AutoText", "CallLog query failed", t)
            null
        } finally {
            c?.close()
        }
    }

    private fun shouldSendTo(number: String): Boolean {
        val n = PhoneUtils.normalize(number)
        if (n.length < 7) return false
        // Avoid sending to short codes, service codes, or our own number (change if needed)
        if (PhoneUtils.isServiceCode(n)) return false
        if (PhoneUtils.isLikelyInternational(n)) {
            // Optional: block international if you don't want int'l SMS charges
            // return false
        }

        val last = prefs.getLong("sent_$n", 0L)
        val now = System.currentTimeMillis()
        return (now - last) >= MIN_INTERVAL_MS
    }

    private fun markSent(number: String) {
        val n = PhoneUtils.normalize(number)
        prefs.edit().putLong("sent_$n", System.currentTimeMillis()).apply()
    }

    private fun buildMessage(): String {
        // Tweak freely
        return "Hey! It's Alejandro with Spartanburg Service Hub—sorry I missed your call. " +
                "You can text me here with what you need (pics welcome). Reply STOP to opt out."
    }

    private fun sendSms(toRaw: String, body: String): Boolean {
        return try {
            val to = PhoneUtils.normalizeForSmsApi(toRaw)
            val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val subId = SubscriptionManager.getDefaultSmsSubscriptionId()
                SmsManager.getSmsManagerForSubscriptionId(subId)
            } else {
                @Suppress("DEPRECATION")
                SmsManager.getDefault()
            }
            val parts = smsManager.divideMessage(body)
            smsManager.sendMultipartTextMessage(to, null, parts, null, null)
            true
        } catch (t: Throwable) {
            Log.e("AutoText", "SMS send failed", t)
            false
        }
    }
}

object PhoneUtils {
    fun normalize(raw: String?): String {
        if (raw.isNullOrBlank()) return ""
        // Remove spaces, dashes, parens; keep leading '+'
        val trimmed = raw.trim()
        val plus = if (trimmed.startsWith("+")) "+" else ""
        val digitsOnly = trimmed.replace(Regex("[^0-9]"), "")
        return plus + digitsOnly
    }

    fun normalizeForSmsApi(raw: String): String {
        val n = normalize(raw)
        // For US numbers missing +1, add it
        return if (!n.startsWith("+") && n.length == 10) "+1$n" else n
    }

    fun isServiceCode(n: String): Boolean {
        // Skip short codes / service numbers
        return n.length < 7
    }

    fun isLikelyInternational(n: String): Boolean {
        return n.startsWith("+") && !n.startsWith("+1")
    }
}
