// MainActivity.kt
package com.clintmoyer.conversight

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.provider.CallLog
import java.util.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_CALL_LOG = 1
    private lateinit var recyclerView: RecyclerView
    private val callData = mutableListOf<CallEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (!hasCallLogPermission()) {
            requestCallLogPermission()
        } else {
            trackCallLogs()
        }
    }

    private fun hasCallLogPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_CALL_LOG
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCallLogPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.READ_CALL_LOG), REQUEST_CODE_CALL_LOG
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_CALL_LOG && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            trackCallLogs()
        }
    }

    private fun trackCallLogs() {
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

        callLogs?.use { cursor ->
            while (cursor.moveToNext()) {
                val number = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER))
                val type = cursor.getInt(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE))
                val date = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))
                val duration = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION))

                callData.add(CallEntry(number, type, date, duration))
            }
        }

        // Set the adapter with the collected call data
        recyclerView.adapter = CallLogAdapter(callData)
    }
}

