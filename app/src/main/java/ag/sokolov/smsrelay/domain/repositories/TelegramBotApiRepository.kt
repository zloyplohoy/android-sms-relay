package ag.sokolov.smsrelay.domain.repositories

import ag.sokolov.smsrelay.domain.models.TelegramBot
import ag.sokolov.smsrelay.domain.models.TelegramPrivateChatMessage
import ag.sokolov.smsrelay.domain.models.TelegramUser
import kotlin.time.Duration

interface TelegramBotApiRepository {
    suspend fun getTelegramBot(botApiToken: String): Result<TelegramBot>

    suspend fun getMessages(
        botApiToken: String, longPollingTimeout: Duration
    ): Result<List<TelegramPrivateChatMessage>>

    suspend fun sendMessage(
        botApiToken: String, text: String, chatId: Long
    ): Result<Unit>

    suspend fun getChat(
        chatId: Long, botApiToken: String
    ): Result<TelegramUser>
}
