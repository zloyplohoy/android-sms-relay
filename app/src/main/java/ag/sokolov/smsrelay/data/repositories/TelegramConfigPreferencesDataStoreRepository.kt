package ag.sokolov.smsrelay.data.repositories

import ag.sokolov.smsrelay.domain.models.TelegramSettings
import ag.sokolov.smsrelay.domain.models.TelegramSettingsStatus
import ag.sokolov.smsrelay.domain.repositories.TelegramConfigRepository
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TelegramConfigPreferencesDataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : TelegramConfigRepository {

    companion object {
        private val BOT_API_TOKEN = stringPreferencesKey("telegram_config_bot_api_token")
        private val RECIPIENT_ID = longPreferencesKey("telegram_config_recipient_id")
    }

    override suspend fun getBotApiToken(): Result<String> = runCatching {
        // TODO: Custom error handling
        val telegramConfig = dataStore.data.first()
        telegramConfig[BOT_API_TOKEN] ?: throw NoSuchElementException("Bot API token not found")
    }

    override suspend fun setBotApiToken(apiKey: String): Result<Unit> = runCatching {
        // TODO: Custom error handling
        dataStore.edit { telegramConfig ->
            telegramConfig[BOT_API_TOKEN] = apiKey
        }
    }

    override suspend fun getRecipientId(): Result<Long> = runCatching {
        val telegramConfig = dataStore.data.first()
        telegramConfig[RECIPIENT_ID] ?: throw NoSuchElementException("Recipient ID not found")
    }

    override suspend fun setRecipientId(recipientId: Long): Result<Unit> = runCatching {
        dataStore.edit { telegramConfig ->
            telegramConfig[RECIPIENT_ID] = recipientId
        }
    }

    override fun getBotApiTokenFlow(): Flow<String?> =
        dataStore.data.map { telegramConfig -> telegramConfig[BOT_API_TOKEN] }

    override fun getRecipientIdFlow(): Flow<Long?> =
        dataStore.data.map { telegramConfig -> telegramConfig[RECIPIENT_ID] }

    override suspend fun deleteBotApiToken(): Result<Unit> = runCatching {
        dataStore.edit { telegramConfig ->
            telegramConfig.remove(BOT_API_TOKEN)
        }
    }
}
