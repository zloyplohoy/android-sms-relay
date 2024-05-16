package ag.sokolov.smsrelay.domain.repository

import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.model.TelegramUser
import kotlinx.coroutines.flow.Flow

interface ConfigurationRepository {
    // Telegram bot API token
    fun getTelegramBotApiToken(): Flow<String?>
    suspend fun setTelegramBotApiToken(botApiToken: String): Result<Unit>
    suspend fun deleteTelegramBotApiToken(): Result<Unit>

    // Telegram bot cache
    fun getCachedTelegramBot(): Flow<TelegramBot?>
    suspend fun setCachedTelegramBot(telegramBot: TelegramBot)
    suspend fun deleteCachedTelegramBot(): Result<Unit>

    // Telegram recipient ID
    fun getTelegramRecipientId(): Flow<Long?>
    suspend fun setTelegramRecipientId(recipientId: Long): Result<Unit>
    suspend fun deleteTelegramRecipientId(): Result<Unit>

    // Telegram recipient cache
    fun getCachedTelegramRecipient(): Flow<TelegramUser?>
    suspend fun setCachedTelegramRecipient(telegramRecipient: TelegramUser)
    suspend fun deleteCachedTelegramRecipient(): Result<Unit>
}
