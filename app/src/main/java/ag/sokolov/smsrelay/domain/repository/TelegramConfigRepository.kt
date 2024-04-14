package ag.sokolov.smsrelay.domain.repository

interface TelegramConfigRepository {
    suspend fun getBotApiToken(): Result<String>
    suspend fun setBotApiToken(apiKey: String): Result<Unit>
}
