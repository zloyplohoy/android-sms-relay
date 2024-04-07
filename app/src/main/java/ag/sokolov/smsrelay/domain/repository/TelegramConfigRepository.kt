package ag.sokolov.smsrelay.domain.repository

interface TelegramConfigRepository {
    suspend fun getBotApiKey(): Result<String?>
    suspend fun setBotApiKey(apiKey: String): Result<Unit>
}
