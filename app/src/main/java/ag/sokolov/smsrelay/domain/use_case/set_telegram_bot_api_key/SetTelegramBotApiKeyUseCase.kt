package ag.sokolov.smsrelay.domain.use_case.set_telegram_bot_api_key

import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository
import ag.sokolov.smsrelay.domain.repository.TelegramConfigRepository
import javax.inject.Inject

class SetTelegramBotApiKeyUseCase @Inject constructor(
    private val telegramConfigRepository: TelegramConfigRepository,
    private val telegramBotApiRepository: TelegramBotApiRepository
) {
    suspend operator fun invoke(botApiToken: String): Result<Unit> {
        if (!botApiToken.matches(botApiTokenRegex)) {
            return Result.failure(IllegalArgumentException("Invalid token format"))
        }
        return telegramBotApiRepository.getBotDetails(botApiToken).onSuccess {
            telegramConfigRepository.setBotApiToken(botApiToken).map { }
        }.map { }
    }

    companion object {
        // Telegram bot API token consists of two fields, separated by a colon:
        // 1. A numeric bot ID
        // 2. A secret string of 35 alphanumeric characters, hyphens and underscores
        private val botApiTokenRegex: Regex = """^\d+:[A-Za-z0-9_-]{35}$""".toRegex()
    }
}
