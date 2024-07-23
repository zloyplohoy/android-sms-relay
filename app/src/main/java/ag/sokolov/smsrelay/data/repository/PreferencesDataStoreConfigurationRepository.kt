package ag.sokolov.smsrelay.data.repository

import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
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

class PreferencesDataStoreConfigurationRepository
@Inject constructor(private val dataStore: DataStore<Preferences>) : ConfigurationRepository {

    companion object {
        private val TELEGRAM_BOT_API_TOKEN = stringPreferencesKey("telegram_bot_api_token")
        private val TELEGRAM_RECIPIENT_ID = longPreferencesKey("telegram_recipient_id")
    }

    override fun getTelegramBotApiToken(): Flow<String?> =
        dataStore.data.map { it[TELEGRAM_BOT_API_TOKEN] }.distinctUntilChanged()

    override suspend fun setTelegramBotApiToken(botApiToken: String): Response<Unit, DomainError> =
        runCatching { dataStore.edit { it[TELEGRAM_BOT_API_TOKEN] = botApiToken } }.fold(
            onSuccess = { Response.Success(Unit) },
            onFailure = { Response.Failure(DomainError.UnhandledError) },
        )

    override suspend fun deleteTelegramApiTokenAndRecipientId(): Result<Unit> =
        runCatching {
            dataStore.edit { telegramConfig ->
                telegramConfig.remove(TELEGRAM_BOT_API_TOKEN)
                telegramConfig.remove(TELEGRAM_RECIPIENT_ID)
            }
        }

    override fun getTelegramRecipientId(): Flow<Long?> =
        dataStore.data.map { it[TELEGRAM_RECIPIENT_ID] }.distinctUntilChanged()

    override suspend fun setTelegramRecipientId(recipientId: Long): Result<Unit> =
        runCatching {
            dataStore.edit { it[TELEGRAM_RECIPIENT_ID] = recipientId }
        }

    override suspend fun deleteTelegramRecipientId(): Result<Unit> =
        runCatching {
            dataStore.edit { it.remove(TELEGRAM_RECIPIENT_ID) }
        }

    override suspend fun getTelegramBotApiToken2(): String? {
        return dataStore.data.first()[TELEGRAM_BOT_API_TOKEN]
    }
}
