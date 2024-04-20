package ag.sokolov.smsrelay.domain.use_case.set_telegram_bot_api_key

import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository
import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository.Companion.isTelegramBotApiTokenStructureValid
import ag.sokolov.smsrelay.domain.repository.TelegramConfigRepository
import javax.inject.Inject

class SetTelegramBotApiKeyUseCase @Inject constructor(
    private val telegramConfigRepository: TelegramConfigRepository,
    private val telegramBotApiRepository: TelegramBotApiRepository
) {
    suspend operator fun invoke(botApiToken: String): Result<Unit> {
        if (!isTelegramBotApiTokenStructureValid(botApiToken)) {
            return Result.failure(IllegalArgumentException("Invalid token format"))
        }
        return telegramBotApiRepository.getBotDetails(botApiToken).onSuccess {
            telegramConfigRepository.setBotApiToken(botApiToken)
        }.map {  }
    }
}
