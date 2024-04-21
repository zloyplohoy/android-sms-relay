package ag.sokolov.smsrelay.domain.use_case.send_telegram_message

import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository
import ag.sokolov.smsrelay.domain.repository.TelegramConfigRepository
import java.io.IOException
import javax.inject.Inject


class SendTelegramMessageUseCase @Inject constructor(
    private val telegramConfigRepository: TelegramConfigRepository,
    private val telegramBotApiRepository: TelegramBotApiRepository
) {
    suspend operator fun invoke(text: String): Result<Unit> = runCatching {
        val botApiToken: String = getTelegramBotApiTokenOrThrow()
        val recipientId: Long = getTelegramRecipientIdOrThrow()
        // TODO: What to do when sending fails?
//        telegramBotApiRepository.sendMessage(botApiToken, text, recipientId).onFailure {
//            throw IOException("Message sending failed")
//        }
        telegramBotApiRepository.sendMessage(botApiToken, text, recipientId).getOrThrow()
    }

    private suspend fun getTelegramBotApiTokenOrThrow(): String =
        telegramConfigRepository.getBotApiToken().getOrThrow()

    private suspend fun getTelegramRecipientIdOrThrow(): Long =
        telegramConfigRepository.getRecipientId().getOrThrow()
}
