package ag.sokolov.smsrelay.domain.use_case.set_telegram_bot_api_key

import ag.sokolov.smsrelay.domain.repository.TelegramConfigRepository
import javax.inject.Inject

class SetTelegramBotApiKeyUseCase @Inject constructor(
    private val telegramConfigRepository: TelegramConfigRepository
) {
    suspend operator fun invoke(apiKey: String): Result<Unit> = runCatching {
        require(apiKey.matches(botApiKeyRegex)) { "Invalid token format" }
        telegramConfigRepository.setBotApiKey(apiKey)
    }

    companion object {
        // Telegram bot API token consists of two fields, separated by a colon:
        // 1. A numeric bot ID
        // 2. A secret string of 35 alphanumeric characters, hyphens and underscores
        private val botApiKeyRegex: Regex = """^\d+:[A-Za-z0-9_-]{35}$""".toRegex()
    }
}
