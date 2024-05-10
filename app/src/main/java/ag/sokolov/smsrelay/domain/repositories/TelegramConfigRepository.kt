package ag.sokolov.smsrelay.domain.repositories

import ag.sokolov.smsrelay.domain.models.TelegramSettings
import kotlinx.coroutines.flow.Flow

interface TelegramConfigRepository {
    suspend fun getBotApiToken(): Result<String>
    suspend fun setBotApiToken(apiKey: String): Result<Unit>
    suspend fun getRecipientId(): Result<Long>
    suspend fun setRecipientId(recipientId: Long): Result<Unit>
    fun getBotApiTokenFlow(): Flow<String?>
    fun getRecipientIdFlow(): Flow<Long?>
    suspend fun deleteBotApiToken(): Result<Unit>
}
