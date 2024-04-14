package ag.sokolov.smsrelay.data.repository

import ag.sokolov.smsrelay.domain.repository.TelegramConfigRepository
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TelegramConfigPreferencesDataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : TelegramConfigRepository {

    companion object {
        private val BOT_API_TOKEN = stringPreferencesKey("telegram_config_bot_api_token")
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
}
