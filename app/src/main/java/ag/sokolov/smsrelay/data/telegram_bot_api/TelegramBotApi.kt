package ag.sokolov.smsrelay.data.telegram_bot_api

import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.model.TelegramPrivateChatMessage
import ag.sokolov.smsrelay.domain.model.TelegramUser
import kotlin.time.Duration

interface TelegramBotApi {

    suspend fun getTelegramBot(
        botApiToken: String
    ): Response<TelegramBot, DomainError>

    suspend fun getTelegramRecipient(
        botApiToken: String,
        recipientId: Long
    ): Response<TelegramUser, DomainError>

    // TODO: Refactor for new approach
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
