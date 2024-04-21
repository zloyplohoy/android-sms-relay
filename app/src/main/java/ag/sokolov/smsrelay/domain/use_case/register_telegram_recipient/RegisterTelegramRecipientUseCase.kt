package ag.sokolov.smsrelay.domain.use_case.register_telegram_recipient

import ag.sokolov.smsrelay.domain.model.TelegramPrivateChatMessage
import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository
import ag.sokolov.smsrelay.domain.repository.TelegramConfigRepository
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class RegisterTelegramRecipientUseCase @Inject constructor(
    private val telegramConfigRepository: TelegramConfigRepository,
    private val telegramBotApiRepository: TelegramBotApiRepository
) {
    suspend operator fun invoke(verificationCode: String, verificationTimeout: Duration): Result<Unit> =
        runCatching {
            // TODO:
            // 3. Save recipient chat ID
            // 4. Return success if a message contains the verification code
            val botApiToken : String = getBotApiTokenOrThrow()
            withTimeout(verificationTimeout) {
                while (true) {
                    // TODO: parametrize long polling timeout
                    telegramBotApiRepository.getMessages(botApiToken, 3.seconds).getOrThrow().forEach { privateChatMessage ->
                        if (isValidVerificationMessage(privateChatMessage, verificationCode)) {
                            // TODO: Save recipient ID
                            telegramConfigRepository.setRecipientId(privateChatMessage.from.id)
                            return@withTimeout
                        }
                    }
                }
            }
        }

    private suspend fun getBotApiTokenOrThrow(): String =
        telegramConfigRepository.getBotApiToken().getOrThrow()

    private fun isValidVerificationMessage(
        message: TelegramPrivateChatMessage,
        verificationCode: String
    ): Boolean {
        return message.text == "/start $verificationCode"
    }
}
