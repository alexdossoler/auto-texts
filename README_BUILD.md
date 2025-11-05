# AutoText Missed Call - Build Instructions

## 🚀 Quick Start: Building the APK

### Option 1: Android Studio (Recommended for Development)

1. **Install Android Studio** from https://developer.android.com/studio
2. **Open this project** in Android Studio
3. Wait for Gradle sync to complete
4. Go to **Build → Build Bundle(s) / APK(s) → Build APK(s)**
5. Find your APK at: `app/build/outputs/apk/debug/app-debug.apk`

### Option 2: GitHub Actions (Automated Cloud Build)

This project includes a GitHub Actions workflow that automatically builds the APK:

1. **Push to GitHub:**
   ```bash
   git add .
   git commit -m "Initial commit"
   git push
   ```

2. **Go to your repo on GitHub** → **Actions** tab
3. Click on the latest workflow run
4. Download the **app-debug** artifact (it's a ZIP containing the APK)

### Option 3: Command Line (requires Android SDK)

```bash
# Make gradlew executable
chmod +x gradlew

# Build the APK
./gradlew assembleDebug

# Find APK at: app/build/outputs/apk/debug/app-debug.apk
```

## 📲 Installing on Your Boost Mobile Phone

1. **Transfer the APK** to your phone (email, USB, cloud storage)
2. **Enable Unknown Sources:**
   - Go to Settings → Security → Unknown Sources (enable)
   - Or Settings → Apps → Special Access → Install unknown apps
3. **Tap the APK** file to install
4. **Open the app** and follow the setup buttons

## ✅ Setup Checklist (Once Installed)

1. ✅ **Grant Permissions** - Tap button 1 and allow all permissions
2. ✅ **Enable Notification Access** - Tap button 2, find "AutoText Missed Call" and enable
3. ✅ **Disable Battery Optimization** - Tap button 3, find the app and allow
4. ✅ **Test** - Have someone call you and don't answer!

## 🔧 Customizing the Auto-Text Message

Edit the message in `MissedCallListenerService.kt`:

```kotlin
private fun buildMessage(): String {
    return "Hey! It's Alejandro with Charlotte Service Hub—sorry I missed your call. " +
            "You can text me here with what you need (pics welcome). Reply STOP to opt out."
}
```

## 📝 Your Phone Number

The app sends from your actual phone number: **+1 407-234-5863** (Boost Mobile)

No external service needed - it uses your phone's SMS capability!
