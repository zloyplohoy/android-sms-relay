package ag.sokolov.smsrelay.ui.setup.screen.recipient

import ag.sokolov.smsrelay.R
import ag.sokolov.smsrelay.domain.service.add_recipient.AddRecipientService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.lang.Thread.sleep

// TODO: This is garbage, needs to be rewritten properly
class RecipientVerificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val pin = inputData.getString("PIN") ?: return Result.failure()
        repeat(5) {
            Log.d("TAG", "doWork: Doing work with $pin, no shit")
            sleep(5_000)
        }
        return Result.success()
    }

    companion object {
        const val CHANNEL_ID = "ShortServiceChannel"
    }

    override fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(1, createNotification())
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            AddRecipientService.CHANNEL_ID,
            "Short Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(applicationContext, NotificationManager::class.java)
        manager?.createNotificationChannel(serviceChannel)
    }

    private fun createNotification(): Notification {
        createNotificationChannel()
        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Recipient registration running")
            .setSmallIcon(R.drawable.ic_launcher_foreground).build()
    }
}
