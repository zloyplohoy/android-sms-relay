package ag.sokolov.smsrelay.domain.repositories

import ag.sokolov.smsrelay.domain.models.TelegramBot
import ag.sokolov.smsrelay.domain.models.TelegramPrivateChatMessage
import kotlin.time.Duration

interface TelegramBotApiRepository {
    suspend fun getBotDetails(botApiToken: String): Result<TelegramBot>

    suspend fun getMessages(
        botApiToken: String,
        longPollingTimeout: Duration
    ): Result<List<TelegramPrivateChatMessage>>

    suspend fun sendMessage(
        botApiToken: String,
        text: String,
        chatId: Long
    ): Result<Unit>
}
