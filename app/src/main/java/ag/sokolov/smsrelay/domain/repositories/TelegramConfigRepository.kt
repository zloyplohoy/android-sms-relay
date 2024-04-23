package ag.sokolov.smsrelay.domain.repositories

import kotlinx.coroutines.flow.Flow

interface TelegramConfigRepository {
    suspend fun getBotApiToken(): Result<String>
    suspend fun setBotApiToken(apiKey: String): Result<Unit>
    suspend fun getRecipientId(): Result<Long>
    suspend fun setRecipientId(recipientId: Long): Result<Unit>
    fun getBotApiTokenFlow(): Flow<String?>
    suspend fun deleteBotApiToken(): Result<Unit>
}
