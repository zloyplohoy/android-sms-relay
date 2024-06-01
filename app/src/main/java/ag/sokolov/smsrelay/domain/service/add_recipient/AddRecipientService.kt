package ag.sokolov.smsrelay.domain.service.add_recipient

import ag.sokolov.smsrelay.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.lang.Thread.sleep

class AddRecipientService : Service() {
    companion object {
        const val CHANNEL_ID = "ShortServiceChannel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        Log.d("TAG", "onStartCommand: startForeground imminent")
        startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE)
        Log.d("TAG", "onStartCommand: startForeground done")
        // Perform your short service task here

        sleep(5000)

        stopSelf() // Stop the service once the task is done
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel =
                NotificationChannel(
                    CHANNEL_ID, "Short Service Channel", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Short Service")
            .setContentText("Running short foreground service")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }
}
