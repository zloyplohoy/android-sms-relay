package ag.sokolov.smsrelay.domain.repository

interface TelegramConfigRepository {
    suspend fun getBotApiToken(): Result<String>
    suspend fun setBotApiToken(apiKey: String): Result<Unit>
    suspend fun getRecipientId(): Result<Long>
    suspend fun setRecipientId(recipientId: Long): Result<Unit>
}
