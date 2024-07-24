package ag.sokolov.smsrelay.work

import ag.sokolov.smsrelay.R
import ag.sokolov.smsrelay.domain.model.Constants.RECIPIENT_VERIFICATION_NOTIFICATION_CHANNEL_ID
import android.app.Notification
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
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

    override fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(1, createNotification())
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(
            applicationContext,
            RECIPIENT_VERIFICATION_NOTIFICATION_CHANNEL_ID
        )
            .setContentTitle("Recipient verification running")
            .setSmallIcon(R.drawable.ic_launcher_foreground).build()
    }
}
