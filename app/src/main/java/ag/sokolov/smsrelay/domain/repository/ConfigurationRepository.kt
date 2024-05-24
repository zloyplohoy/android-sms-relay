package ag.sokolov.smsrelay.domain.repository

import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface ConfigurationRepository {
    // Telegram bot API token
    fun getTelegramBotApiToken(): Flow<String?>

    suspend fun setTelegramBotApiToken(botApiToken: String): Response<Unit, DomainError>

    suspend fun deleteTelegramApiTokenAndRecipientId(): Result<Unit>

    // Telegram recipient ID
    fun getTelegramRecipientId(): Flow<Long?>

    suspend fun setTelegramRecipientId(recipientId: Long): Result<Unit>

    suspend fun deleteTelegramRecipientId(): Result<Unit>
}
