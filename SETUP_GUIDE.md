# 📱 AutoText Missed Call - Complete Setup Guide

## What This App Does

Automatically sends an SMS from **your phone number (+1 407-234-5863)** when you miss a call.

**Message sent:** 
> "Hey! It's Alejandro with Charlotte Service Hub—sorry I missed your call. You can text me here with what you need (pics welcome). Reply STOP to opt out."

---

## 🏗️ Step 1: Build the APK

### Option A: Use GitHub Actions (Easiest - No Android Studio Needed!)

1. **Initialize Git repository** (if not already done):
   ```bash
   cd /home/alex/saas/auto-texts
   git init
   git add .
   git commit -m "Initial AutoText app"
   ```

2. **Push to GitHub:**
   ```bash
   # Create a new repo on GitHub first, then:
   git remote add origin https://github.com/YOUR_USERNAME/auto-texts.git
   git branch -M main
   git push -u origin main
   ```

3. **Download the APK:**
   - Go to your GitHub repo → **Actions** tab
   - Click the latest workflow run
   - Download **app-debug** artifact (ZIP file)
   - Extract to get `app-debug.apk`

### Option B: Use Android Studio

1. Transfer this folder to a Windows/Mac/Linux x86 computer
2. Open in Android Studio
3. Build → Build Bundle(s) / APK(s) → Build APK(s)
4. Find APK at `app/build/outputs/apk/debug/app-debug.apk`

---

## 📲 Step 2: Install on Your Boost Mobile Phone

1. **Transfer APK** to your phone:
   - Email yourself the APK
   - Or use Google Drive/Dropbox
   - Or USB: `adb install app-debug.apk`

2. **Enable installation from unknown sources:**
   - Settings → Security → **Unknown Sources** (enable)
   - OR Settings → Apps → Special Access → **Install unknown apps** → Allow

3. **Tap the APK file** on your phone to install

---

## ✅ Step 3: Configure the App (Critical!)

Open the AutoText app and tap each button IN ORDER:

### 1️⃣ Grant Permissions
Tap **"1) Grant Permissions"** and allow:
- ✅ **Phone** (READ_PHONE_STATE)
- ✅ **Call logs** (READ_CALL_LOG)  
- ✅ **SMS** (SEND_SMS)
- ✅ **Notifications** (Android 13+)

### 2️⃣ Enable Notification Access (MOST IMPORTANT!)
Tap **"2) Open Notification Access"**
- Find **"AutoText Missed Call"** in the list
- **Toggle it ON**
- This allows the app to detect missed call notifications

### 3️⃣ Disable Battery Optimization
Tap **"3) Disable Battery Optimization"**
- Find **"AutoText Missed Call"**
- Select **"Don't optimize"**
- This prevents Android from killing the app

### 4️⃣ Test SMS (Optional)
Tap **"Send Test SMS to +1 407-234-5863"**
- Sends a test message to yourself
- Verifies SMS permission is working

---

## 🧪 Step 4: Test It!

1. Have a friend/another phone call your Boost number: **+1 407-234-5863**
2. **Don't answer** the call
3. Within 2-5 seconds of the missed call notification, they should receive:
   > "Hey! It's Alejandro with Charlotte Service Hub—sorry I missed your call..."

---

## 🔧 Features & Behavior

✅ **15-minute cooldown** per number (won't spam the same person)  
✅ **Works with your actual phone number** (no external service needed)  
✅ **Supports English & Spanish** ("Missed call" / "Llamada perdida")  
✅ **Filters short codes** (won't reply to service numbers)  
✅ **Dual-SIM aware** (uses your default SMS subscription)  
✅ **Rate-limited** to prevent accidental spam  

---

## 🎨 Customization

### Change the Auto-Text Message

Edit `app/src/main/java/com/charlotteservicehub/autotext/MissedCallListenerService.kt`:

```kotlin
private fun buildMessage(): String {
    return "Your custom message here!"
}
```

### Adjust Rate Limiting (Default: 15 minutes)

In `MissedCallListenerService.kt`:

```kotlin
private val MIN_INTERVAL_MS = 15 * 60 * 1000L  // Change 15 to desired minutes
```

### Add Quiet Hours (Don't send 9pm-8am)

Add to `shouldSendTo()` method:

```kotlin
val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
if (hour < 8 || hour >= 21) return false  // 9pm-8am quiet
```

---

## 🐛 Troubleshooting

### Auto-text not sending?

1. **Check Notification Access** is enabled (most common issue!)
2. **Check Battery Optimization** is disabled
3. **Verify SMS permission** was granted
4. Check logcat: `adb logcat | grep AutoText`

### Wrong number in notification?

Some devices/OEMs don't include the number in the notification. The app falls back to reading the call log (last 2 minutes).

### Dual-SIM issues?

Make sure your **Boost SIM** is set as the default for SMS:
- Settings → SIM cards → Default for SMS → Select Boost SIM

---

## 📝 Technical Details

- **Package:** `com.charlotteservicehub.autotext`
- **Min SDK:** Android 6.0 (API 23)
- **Target SDK:** Android 14 (API 34)
- **Permissions:** CALL_LOG, SMS, PHONE_STATE, POST_NOTIFICATIONS, BOOT_COMPLETED
- **Service:** NotificationListenerService
- **Storage:** SharedPreferences (rate limiting data)

---

## 🚀 Ready to Go!

Your app is now set up and ready to handle missed calls automatically. Every missed call will trigger an instant text from your Boost number!

**Questions or need help?** Check the code comments or rebuild with customizations.
