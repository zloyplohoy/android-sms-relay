package ag.sokolov.smsrelay.data.telegram_bot_api

import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.model.TelegramPrivateChatMessage
import ag.sokolov.smsrelay.domain.model.TelegramUser
import kotlinx.coroutines.flow.Flow

interface TelegramBotApi {

    fun getTelegramBotFlow(): Flow<Response<TelegramBot?, DomainError>>
    fun getTelegramRecipientFlow(): Flow<Response<TelegramUser?, DomainError>>

    fun getMessagesFlow(): Flow<TelegramPrivateChatMessage>

    suspend fun sendMessage(message: String)
}
