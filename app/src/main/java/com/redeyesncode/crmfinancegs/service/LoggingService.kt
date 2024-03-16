package com.redeyesncode.crmfinancegs.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.*

class LoggingService : Service() {

    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        job = scope.launch {
            while (isActive) {
                delay(10000) // 10 seconds delay
                logMessage()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel() // Cancel the coroutine job when the service is destroyed
    }

    private fun logMessage() {
        // Your logging logic here
        println("Logging every 10 seconds")
    }
}
