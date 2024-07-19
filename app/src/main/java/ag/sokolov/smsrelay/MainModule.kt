package ag.sokolov.smsrelay

import ag.sokolov.smsrelay.data.repository.AndroidSystemRepositoryImpl
import ag.sokolov.smsrelay.data.repository.PreferencesDataStoreConfigurationRepository
import ag.sokolov.smsrelay.data.telegram_bot_api.TelegramBotApiImpl
import ag.sokolov.smsrelay.data.telegram_bot_api.remote.TelegramBotApiService
import ag.sokolov.smsrelay.data.telegram_bot_api.remote.TelegramBotApiServiceImpl
import ag.sokolov.smsrelay.domain.repository.AndroidSystemRepository
import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import ag.sokolov.smsrelay.data.telegram_bot_api.TelegramBotApi
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MainModule {

    @Binds
    abstract fun bindAndroidSystemRepository(
        impl: AndroidSystemRepositoryImpl
    ): AndroidSystemRepository

    @Binds
    abstract fun bindConfigurationRepository(
        impl: PreferencesDataStoreConfigurationRepository
    ): ConfigurationRepository

    @Binds
    abstract fun bindTelegramBotApiRepository(
        impl: TelegramBotApiImpl
    ): TelegramBotApi

    companion object {
        private val Context.dataStore by preferencesDataStore(name = "application_configuration",
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() })

        @Provides
        @Singleton
        fun provideApplicationConfigurationPreferencesDataStore(
            @ApplicationContext appContext: Context
        ): DataStore<Preferences> =
            appContext.dataStore

        @Provides
        @Singleton
        fun provideTelegramBotApi(): TelegramBotApiService {
            val json = Json { ignoreUnknownKeys = true }
            val jsonMediaType = "application/json".toMediaType()

            return Retrofit.Builder().baseUrl("https://api.telegram.org/")
                .addConverterFactory(json.asConverterFactory(jsonMediaType)).build()
                .create(TelegramBotApiServiceImpl::class.java)
        }
    }
}
