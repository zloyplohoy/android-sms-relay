package ag.sokolov.smsrelay.data.telegram_config

import ag.sokolov.smsrelay.core.datastore.TelegramConfigDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TelegramConfigRepositoryImpl @Inject constructor(
    private val telegramConfigDataSource: TelegramConfigDataSource
) : TelegramConfigRepository {

    // Telegram bot API token

    override suspend fun getToken(): String? =
        telegramConfigDataSource.botApiToken.first()

    override fun getTokenFlow(): Flow<String?> =
        telegramConfigDataSource.botApiToken

    override suspend fun setToken(token: String) {
        telegramConfigDataSource.setBotApiToken(token)
    }

    override suspend fun deleteToken() {
        telegramConfigDataSource.deleteBotApiToken()
    }

    // Telegram recipient ID

    override suspend fun getRecipientId(): Long? =
        telegramConfigDataSource.recipientId.first()

    override fun getRecipientIdFlow(): Flow<Long?> =
        telegramConfigDataSource.recipientId

    override suspend fun setRecipientId(recipientId: Long) {
        telegramConfigDataSource.setRecipientId(recipientId)
    }

    override suspend fun deleteRecipientId() {
        telegramConfigDataSource.deleteRecipientId()
    }
}
