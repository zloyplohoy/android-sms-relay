package ag.sokolov.smsrelay.work

import ag.sokolov.smsrelay.R
import ag.sokolov.smsrelay.data.constants.Constants.RECIPIENT_VERIFICATION_NOTIFICATION_CHANNEL_ID
import ag.sokolov.smsrelay.data.constants.Constants.RECIPIENT_VERIFICATION_NOTIFICATION_TITLE
import ag.sokolov.smsrelay.data.constants.Constants.RECIPIENT_VERIFICATION_TIMEOUT
import ag.sokolov.smsrelay.data.telegram_bot_api.TelegramBotApi
import ag.sokolov.smsrelay.data.telegram_config.TelegramConfig
import ag.sokolov.smsrelay.domain.model.TelegramPrivateChatMessage
import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.timeout

@HiltWorker
class RecipientRegistrationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val telegramConfig: TelegramConfig,
    private val telegramBotApi: TelegramBotApi
) : CoroutineWorker(appContext, workerParams) {

    private val verificationCode: String? = inputData.getString("VERIFICATION_CODE")

    @OptIn(FlowPreview::class)
    override suspend fun doWork(): Result =
        telegramBotApi.getMessagesFlow()
            .takeWhile { !isStopped }
            .timeout(RECIPIENT_VERIFICATION_TIMEOUT)
            .filter { isValidVerificationMessage(it) }
            .firstOrNull()
            ?.let { validVerificationMessage ->
                telegramConfig.setRecipientId(validVerificationMessage.from.id)
                Result.success()
            } ?: Result.failure()

    private fun isValidVerificationMessage(message: TelegramPrivateChatMessage): Boolean =
        verificationCode?.let { message.text == "/start $verificationCode" } ?: false

    // On API < 32 WorkManager starts expedited workers
    // as foreground services which require notifications

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(1, createNotification())
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(
            applicationContext, RECIPIENT_VERIFICATION_NOTIFICATION_CHANNEL_ID
        ).setContentTitle(RECIPIENT_VERIFICATION_NOTIFICATION_TITLE)
            .setSmallIcon(R.drawable.ic_launcher_foreground).build()
    }
}
