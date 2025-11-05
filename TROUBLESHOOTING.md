# 🔧 Troubleshooting: "Restricted Setting" Block

## ⚠️ Common Issue: Greyed Out Permissions

If you see:
- **"App was denied access to SMS"**
- Notification Access toggle is **greyed out** with "**Controlled by Restricted Setting**"

This is Android's security feature for sideloaded apps. **Fix it once and everything works.**

---

## ✅ Fix: Allow the App's Restricted Settings

### Step 1: Open App Info
- Long-press the **AutoText Missed Call** app icon → **App info**
- OR Settings → Apps → **AutoText Missed Call**

### Step 2: Enable the Hidden Switch ⭐ (MOST IMPORTANT!)
1. In App info, tap the **⋮ three-dot menu** (top-right corner)
2. Tap **"Allow restricted setting"**
3. **Confirm the warning**

### Step 3: Grant Permissions Again
1. **App info → Permissions** → Allow:
   - ✅ **SMS**
   - ✅ **Call logs**
   - ✅ **Phone**

2. **Notification Access:**
   - App info → Notifications → Notification access
   - Toggle **AutoText Missed Call** to **ON**

### Step 4: Disable Battery Optimization
- App info → Battery → Set to **Unrestricted**

---

## 🔍 If You Don't See "Allow Restricted Setting"

### Try ADB Method (Requires PC):
```bash
adb shell pm grant com.charlotteservicehub.autotext android.permission.SEND_SMS
adb shell pm grant com.charlotteservicehub.autotext android.permission.READ_CALL_LOG
adb shell pm grant com.charlotteservicehub.autotext android.permission.READ_PHONE_STATE
```

---

## 🧪 Quick Test
1. Call your Boost number (+1 407-234-5863) from another phone
2. **Don't answer** the call
3. Auto-reply should arrive in **1-3 seconds**

---

**Key:** Step 2 - **"Allow restricted setting"** in the three-dot menu! 🔑
