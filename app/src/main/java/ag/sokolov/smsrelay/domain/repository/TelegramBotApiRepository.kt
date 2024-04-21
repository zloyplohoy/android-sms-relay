package ag.sokolov.smsrelay.domain.repository

import ag.sokolov.smsrelay.domain.model.BotDetails
import ag.sokolov.smsrelay.domain.model.TelegramPrivateChatMessage
import kotlin.time.Duration

interface TelegramBotApiRepository {
    suspend fun getBotDetails(botApiToken: String): Result<BotDetails>
    suspend fun getMessages(
        botApiToken: String, longPollingTimeout: Duration
    ): Result<List<TelegramPrivateChatMessage>>

    companion object {
        // Telegram bot API token consists of two fields, separated by a colon:
        // 1. A numeric bot ID
        // 2. A secret string of 35 alphanumeric characters, hyphens and underscores
        private val botApiTokenRegex: Regex = """^\d+:[A-Za-z0-9_-]{35}$""".toRegex()

        fun isTelegramBotApiTokenStructureValid(botApiToken: String): Boolean {
            return botApiToken.matches(botApiTokenRegex)
        }
    }
}
