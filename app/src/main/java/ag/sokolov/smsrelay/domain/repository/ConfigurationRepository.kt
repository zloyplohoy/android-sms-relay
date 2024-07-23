package ag.sokolov.smsrelay.domain.repository

import kotlinx.coroutines.flow.Flow

interface ConfigurationRepository {
    // Telegram bot API token
    suspend fun getTelegramBotApiToken(): String?
    suspend fun setTelegramBotApiToken(botApiToken: String)
    suspend fun deleteTelegramApiToken()

    // Telegram recipient ID
    fun getTelegramRecipientId(): Flow<Long?>

    suspend fun setTelegramRecipientId(recipientId: Long): Result<Unit>

    suspend fun deleteTelegramRecipientId(): Result<Unit>
}
