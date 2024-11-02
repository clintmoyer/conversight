// MainActivity.kt
package com.brigittebernard.conversight

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.provider.CallLog
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_CALL_LOG = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Make sure you have a layout file called `activity_main.xml`

        if (!hasCallLogPermission()) {
            requestCallLogPermission()
        } else {
            // Initialize tracking if permission is already granted
            trackCallLogs()
        }
    }

    private fun hasCallLogPermission(): Boolean {
        // Checks if the app has permission to read call logs
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_CALL_LOG
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCallLogPermission() {
        // Requests the READ_CALL_LOG permission
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.READ_CALL_LOG), REQUEST_CODE_CALL_LOG
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CALL_LOG && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted, start tracking
            trackCallLogs()
        } else {
            // Permission denied, handle gracefully
            Toast.makeText(this, "Permission required to track call logs.", Toast.LENGTH_LONG).show()
        }
    }

    private fun trackCallLogs() {
        // Placeholder function to access and analyze call logs
        val callLogs = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            arrayOf(
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION
            ),
            null, null, CallLog.Calls.DATE + " DESC"
        )

        // Process call data here
        callLogs?.use { cursor ->
            while (cursor.moveToNext()) {
                val number = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER))
                val type = cursor.getInt(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE))
                val date = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))
                val duration = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION))

                // Here, you can log or display call data, analyze patterns, etc.
                println("Call from: $number, Type: $type, Date: $date, Duration: $duration")
            }
        }
    }
}

