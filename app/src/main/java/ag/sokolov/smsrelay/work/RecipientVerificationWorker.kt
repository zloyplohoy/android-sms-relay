package ag.sokolov.smsrelay.work

import ag.sokolov.smsrelay.R
import ag.sokolov.smsrelay.domain.model.Constants.RECIPIENT_VERIFICATION_NOTIFICATION_CHANNEL_ID
import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import android.app.Notification
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.lang.Thread.sleep

// TODO: This is garbage, needs to be rewritten properly
@HiltWorker
class RecipientVerificationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val configurationRepository: ConfigurationRepository
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val token = configurationRepository.getTelegramBotApiToken()
        val pin = inputData.getString("PIN") ?: return Result.failure()
        repeat(5) {
            if (isStopped) {
                Log.d("TAG", "doWork: Stopped $pin")
                return Result.failure()
            }
            Log.d("TAG", "doWork: Doing work with $pin, token is $token")
            sleep(5_000)
        }
        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
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
