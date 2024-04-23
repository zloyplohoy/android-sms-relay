package ag.sokolov.smsrelay.domain.repositories

import ag.sokolov.smsrelay.domain.models.TelegramBotInfo
import ag.sokolov.smsrelay.domain.models.TelegramPrivateChatMessage
import kotlin.time.Duration

interface TelegramBotApiRepository {
    suspend fun getBotInfo(botApiToken: String): Result<TelegramBotInfo>

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
