package ag.sokolov.smsrelay.data.telegram_config

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TelegramConfigImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : TelegramConfig {

    companion object {
        private val TOKEN = stringPreferencesKey("telegram_config_bot_api_token")
        private val RECIPIENT_ID = longPreferencesKey("telegram_config_recipient_id")
    }

    // Telegram bot API token

    override suspend fun getToken(): String? =
        dataStore.data.first()[TOKEN]

    override fun getTokenFlow(): Flow<String?> =
        dataStore.data.map { it[TOKEN] }.distinctUntilChanged()

    override suspend fun setToken(token: String) {
        dataStore.edit { it[TOKEN] = token }
    }

    override suspend fun deleteToken() {
        dataStore.edit { it.remove(TOKEN) }
    }

    // Telegram recipient ID

    override suspend fun getRecipientId(): Long? =
        dataStore.data.first()[RECIPIENT_ID]

    override fun getRecipientIdFlow(): Flow<Long?> =
        dataStore.data.map { it[RECIPIENT_ID] }.distinctUntilChanged()

    override suspend fun setRecipientId(recipientId: Long) {
        dataStore.edit { it[RECIPIENT_ID] = recipientId }
    }

    override suspend fun deleteRecipientId() {
        dataStore.edit { it.remove(RECIPIENT_ID) }
    }
}
