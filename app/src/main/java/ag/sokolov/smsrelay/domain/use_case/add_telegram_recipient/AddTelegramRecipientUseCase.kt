package ag.sokolov.smsrelay.domain.use_case.add_telegram_recipient

import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository
import javax.inject.Inject

class AddTelegramRecipientUseCase
@Inject
constructor(
    private val configurationRepository: ConfigurationRepository,
    private val telegramBotApiRepository: TelegramBotApiRepository
) {
    suspend operator fun invoke(): Result<Unit> =
        configurationRepository.setTelegramRecipientId(1237182748)
    //        runCatching {
    //            // TODO:
    //            // 3. Save recipient chat ID
    //            // 4. Return success if a message contains the verification code
    //            val botApiToken : String = getBotApiTokenOrThrow()
    //            withTimeout(verificationTimeout) {
    //                while (true) {
    //                    // TODO: parametrize long polling timeout
    //                    telegramBotApiRepository.getMessages(botApiToken,
    // 3.seconds).getOrThrow().forEach { privateChatMessage ->
    //                        if (isValidVerificationMessage(privateChatMessage, verificationCode))
    // {
    //                            // TODO: Save recipient ID
    //                            configurationRepository.setRecipientId(privateChatMessage.from.id)
    //                            return@withTimeout
    //                        }
    //                    }
    //                }
    //            }
    //        }

    //    private suspend fun getBotApiTokenOrThrow(): String =
    //        configurationRepository.getBotApiToken().getOrThrow()

    //    private fun isValidVerificationMessage(
    //        message: TelegramPrivateChatMessage,
    //        verificationCode: String
    //    ): Boolean {
    //        return message.text == "/start $verificationCode"
    //    }
}
