package com.charlotteservicehub.autotext

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.charlotteservicehub.autotext.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var vb: ActivityMainBinding

    private val reqPerms = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { /* no-op; we'll rely on user to grant */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        vb.btnGrantPerms.setOnClickListener {
            val perms = mutableListOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.SEND_SMS,
            )
            if (Build.VERSION.SDK_INT >= 33) {
                perms += Manifest.permission.POST_NOTIFICATIONS
            }
            reqPerms.launch(perms.toTypedArray())
        }

        vb.btnOpenNotifAccess.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }

        vb.btnDisableBatteryOpt.setOnClickListener {
            // Ask user to whitelist app from battery optimizations
            val pkg = packageName
            val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
            // Or direct: ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS with data=package:<pkg>
            startActivity(intent)
        }

        vb.btnTestSms.setOnClickListener {
            // Optional: test by sending yourself a text
            val uri = Uri.parse("sms:+14072345863") // your Boost number
            val i = Intent(Intent.ACTION_VIEW, uri)
            i.putExtra("sms_body", "Test from AutoText app.")
            startActivity(i)
        }
    }
}
