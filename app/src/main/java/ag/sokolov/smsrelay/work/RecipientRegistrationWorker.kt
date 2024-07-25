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
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull

// TODO: This is garbage, needs to be rewritten properly
@HiltWorker
class RecipientRegistrationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val configurationRepository: ConfigurationRepository,
    private val telegramBotApi: TelegramBotApi
) : CoroutineWorker(appContext, workerParams) {

    private val verificationCode = inputData.getString("VERIFICATION_CODE")

    override suspend fun doWork(): Result {

        // TODO: Remove
        Log.d("TAG", "doWork: code is $verificationCode")
        delay(10_000)

        verificationCode ?: return Result.failure()
        val telegramBotApiToken =
            configurationRepository.getTelegramBotApiToken() ?: return Result.failure()

        return withTimeoutOrNull(RECIPIENT_VERIFICATION_TIMEOUT) {
            while (!isStopped) {
                getRecipientVerificationResult(telegramBotApiToken)?.let { return@withTimeoutOrNull it }
            }
            Result.failure()
        } ?: Result.failure()
    }

    private suspend fun getRecipientVerificationResult(telegramBotApiToken: String): Result? {
        return when (val apiResponse = telegramBotApi.getMessages(telegramBotApiToken)) {
            is Response.Success -> {
                apiResponse.data.find { isValidVerificationMessage(it) }?.let { Result.success() }
            }
            else -> {
                Result.failure()
            }
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
