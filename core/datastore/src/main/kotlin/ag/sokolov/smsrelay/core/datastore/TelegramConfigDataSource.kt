package ag.sokolov.smsrelay.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TelegramConfigDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private val TOKEN = stringPreferencesKey("telegram_config_bot_api_token")
        private val RECIPIENT_ID = longPreferencesKey("telegram_config_recipient_id")
    }

    val botApiToken: Flow<String?> = dataStore.data.map { it[TOKEN] }.distinctUntilChanged()
    val recipientId: Flow<Long?> = dataStore.data.map { it[RECIPIENT_ID] }.distinctUntilChanged()

    suspend fun setBotApiToken(token: String) {
        dataStore.edit { it[TOKEN] = token }
    }

    suspend fun deleteBotApiToken() {
        dataStore.edit { it.remove(TOKEN) }
    }

    suspend fun setRecipientId(recipientId: Long) {
        dataStore.edit { it[RECIPIENT_ID] = recipientId }
    }

    suspend fun deleteRecipientId() {
        dataStore.edit { it.remove(RECIPIENT_ID) }
    }
}
