package ag.sokolov.smsrelay.work

import ag.sokolov.smsrelay.R
import ag.sokolov.smsrelay.constants.Constants.RECIPIENT_VERIFICATION_NOTIFICATION_CHANNEL_ID
import ag.sokolov.smsrelay.constants.Constants.RECIPIENT_VERIFICATION_NOTIFICATION_TITLE
import ag.sokolov.smsrelay.constants.Constants.RECIPIENT_VERIFICATION_TIMEOUT
import ag.sokolov.smsrelay.data.telegram_bot_api.TelegramBotApi
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramPrivateChatMessage
import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.withTimeoutOrNull

@HiltWorker
class RecipientRegistrationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val configurationRepository: ConfigurationRepository,
    private val telegramBotApi: TelegramBotApi
) : CoroutineWorker(appContext, workerParams) {

    private val verificationCode: String? = inputData.getString("VERIFICATION_CODE")

    override suspend fun doWork(): Result {
        verificationCode ?: return Result.failure()
        val telegramBotApiToken =
            configurationRepository.getTelegramBotApiToken() ?: return Result.failure()
        return registerRecipient(telegramBotApiToken)
    }

    private suspend fun registerRecipient(telegramBotApiToken: String): Result =
        withTimeoutOrNull(RECIPIENT_VERIFICATION_TIMEOUT) {
            while (!isStopped) {
                findVerificationMessage(telegramBotApiToken)?.let {
                    return@withTimeoutOrNull Result.success()
                }
            }
            Result.failure()
        } ?: Result.failure()

    private suspend fun findVerificationMessage(telegramBotApiToken: String): TelegramPrivateChatMessage? {
        return (telegramBotApi.getMessages(telegramBotApiToken) as? Response.Success)?.data?.find {
            isValidVerificationMessage(it)
        }
    }

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
