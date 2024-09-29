package ag.sokolov.smsrelay.core.data.repository

import kotlinx.coroutines.flow.Flow

interface TelegramConfigRepository {

    // Telegram bot API token
    suspend fun getToken(): String?
    fun getTokenFlow(): Flow<String?>
    suspend fun setToken(token: String)
    suspend fun deleteToken()

    // Telegram recipient ID
    suspend fun getRecipientId(): Long?
    fun getRecipientIdFlow(): Flow<Long?>
    suspend fun setRecipientId(recipientId: Long)
    suspend fun deleteRecipientId()
}
