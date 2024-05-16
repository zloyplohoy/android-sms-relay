package ag.sokolov.smsrelay.data.repository

import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.model.TelegramUser
import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ConfigurationPreferencesDataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ConfigurationRepository {

    companion object {
        private val BOT_API_TOKEN = stringPreferencesKey("telegram_bot_api_token")
        private val BOT_NAME = stringPreferencesKey("telegram_bot_name")
        private val BOT_USERNAME = stringPreferencesKey("telegram_bot_username")
        private val RECIPIENT_ID = longPreferencesKey("telegram_recipient_id")
        private val RECIPIENT_FIRST_NAME = stringPreferencesKey("telegram_recipient_first_name")
        private val RECIPIENT_LAST_NAME = stringPreferencesKey("telegram_recipient_last_name")
        private val RECIPIENT_USERNAME = stringPreferencesKey("telegram_recipient_username")
    }

    override suspend fun setTelegramBotApiToken(botApiToken: String): Result<Unit> = runCatching {
        dataStore.edit {
            it[BOT_API_TOKEN] = botApiToken
        }
    }

    override suspend fun setTelegramRecipientId(recipientId: Long): Result<Unit> = runCatching {
        dataStore.edit {
            it[RECIPIENT_ID] = recipientId
        }
    }

    override suspend fun deleteTelegramBotApiToken(): Result<Unit> = runCatching {
        dataStore.edit { telegramConfig ->
            telegramConfig.remove(BOT_API_TOKEN)
        }
    }

    override fun getCachedTelegramBot(): Flow<TelegramBot?> = dataStore.data.map {
        // TODO: Refactor
        if (it[BOT_NAME] != null && it[BOT_USERNAME] != null) {
            TelegramBot(it[BOT_NAME]!!, it[BOT_USERNAME]!!)
        } else {
            null
        }
    }.distinctUntilChanged()

    override suspend fun setCachedTelegramBot(telegramBot: TelegramBot) {
        dataStore.edit {
            it[BOT_NAME] = telegramBot.name
            it[BOT_USERNAME] = telegramBot.username
        }
    }

    override fun getTelegramBotApiToken(): Flow<String?> = dataStore.data.map {
        it[BOT_API_TOKEN]
    }.distinctUntilChanged()

    override fun getTelegramRecipientId(): Flow<Long?> = dataStore.data.map {
        it[RECIPIENT_ID]
    }.distinctUntilChanged()

    override suspend fun setCachedTelegramRecipient(telegramRecipient: TelegramUser) {
        dataStore.edit {
            it[RECIPIENT_FIRST_NAME] = telegramRecipient.firstName
            telegramRecipient.lastName?.let { lastName ->
                it[RECIPIENT_LAST_NAME] = lastName
            }
            telegramRecipient.username?.let { username ->
                it[RECIPIENT_USERNAME] = username
            }
        }
    }

    override fun getCachedTelegramRecipient(): Flow<TelegramUser?> = dataStore.data.map {
        it[RECIPIENT_FIRST_NAME]?.let { firstName ->
            TelegramUser(
                firstName = firstName,
                lastName = it[RECIPIENT_LAST_NAME],
                username = it[BOT_USERNAME]
            )
        }
    }

    override suspend fun deleteCachedTelegramBot(): Result<Unit> = runCatching {
        dataStore.edit {
            it.remove(BOT_NAME)
            it.remove(BOT_USERNAME)
        }
    }

    override suspend fun deleteCachedTelegramRecipient(): Result<Unit> = runCatching {
        dataStore.edit {
            it.remove(RECIPIENT_FIRST_NAME)
            it.remove(RECIPIENT_LAST_NAME)
            it.remove(RECIPIENT_USERNAME)
        }
    }

    override suspend fun deleteTelegramRecipientId(): Result<Unit> = runCatching {
        dataStore.edit {
            it.remove(RECIPIENT_ID)
        }
    }
}
