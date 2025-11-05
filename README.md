# AutoText Missed Call - Android App

A minimal Android app that automatically texts callers when you miss their call. Designed for sideload/private use on your Boost Mobile device (+1 407-234-5863).

## Features

- Detects missed calls via system notifications
- Automatically sends SMS from your own number
- Rate limiting (15 minutes between texts to same number)
- Filters out short codes and service numbers
- Works with Boost Mobile and other carriers

## Project Setup

### Requirements
- Android Studio (latest version recommended)
- minSdk 23, targetSdk 34
- Kotlin

### Build Instructions

1. Open this project in Android Studio
2. Wait for Gradle sync to complete
3. Build APK: `Build > Build Bundle(s) / APK(s) > Build APK(s)`
4. Locate APK in `app/build/outputs/apk/debug/`

## Installation & Setup

### 1. Install the APK
Transfer the APK to your Boost Mobile phone and install (enable "Install from Unknown Sources" if needed).

### 2. Grant Permissions
Open the app and tap through the setup buttons:

- **Button 1:** Grant Permissions
  - Phone (READ_PHONE_STATE)
  - Call logs (READ_CALL_LOG)
  - SMS (SEND_SMS)
  - Notifications (Android 13+)

### 3. Enable Notification Access
- **Button 2:** Open Notification Access Settings
- Find "AutoText Missed Call" and enable it
- This is critical - the app won't work without this

### 4. Disable Battery Optimization
- **Button 3:** Battery Optimization Settings
- Find the app and exclude it from battery optimization
- Prevents the system from killing the service

### 5. Dual-SIM Setup (if applicable)
If you have multiple SIMs, ensure your Boost SIM is set as default for SMS in system settings.

## Testing

1. From another phone, call your Boost number
2. Don't answer - let it go to voicemail
3. Within seconds, the caller should receive:

> "Hey! It's Alejandro with Charlotte Service Hub—sorry I missed your call. You can text me here with what you need (pics welcome). Reply STOP to opt out."

You can also use **Button 4** to send a test SMS.

## Customization

### Change the Auto-Reply Message
Edit `MissedCallListenerService.kt`, function `buildMessage()`:

```kotlin
private fun buildMessage(): String {
    return "Your custom message here"
}
```

### Adjust Rate Limiting
Change the interval in `MissedCallListenerService.kt`:

```kotlin
private val MIN_INTERVAL_MS = 15 * 60 * 1000L  // 15 minutes
```

### Add Quiet Hours
In `shouldSendTo()`, add time checks:

```kotlin
val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
if (hour < 8 || hour >= 21) return false  // Skip 9pm-8am
```

### Block/Allow Lists
Store numbers in SharedPreferences and check before sending.

## Known Limitations

- **Dialer package detection:** Different Android OEMs use different dialer packages. The app checks for common ones (Google Dialer, AOSP). May need adjustment for your device.
- **Notification format:** Missed call notification text varies by locale. Currently handles English/Spanish.
- **Play Store:** This app uses READ_CALL_LOG and SEND_SMS which have Google Play policy restrictions. Perfect for sideload/private use.

## Troubleshooting

### Auto-texts not sending?
1. Check Notification Access is enabled
2. Verify all permissions granted
3. Check logcat for "AutoText" logs
4. Ensure Boost SIM is default for SMS

### Battery optimization killing service?
Go back to Settings and ensure the app is unrestricted.

### Wrong SMS sender?
If dual-SIM, set Boost as default SMS SIM in system settings.

## Future Enhancements

Possible additions:
- Unknown callers only mode
- Configurable quiet hours in UI
- Toggle switch to enable/disable service
- Message template editor
- CSV logging of auto-texts sent
- Contact integration (different messages for contacts vs unknown)

## License

Private use - not for distribution.

---

**Contact:** Alejandro - Charlotte Service Hub - +1 407-234-5863
