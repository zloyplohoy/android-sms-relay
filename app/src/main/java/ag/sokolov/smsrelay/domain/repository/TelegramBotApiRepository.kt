package ag.sokolov.smsrelay.domain.repository

import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.model.TelegramPrivateChatMessage
import ag.sokolov.smsrelay.domain.model.TelegramUser
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
