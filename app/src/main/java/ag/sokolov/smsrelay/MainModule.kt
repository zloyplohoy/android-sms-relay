package ag.sokolov.smsrelay

import ag.sokolov.smsrelay.data.repository.TelegramBotApiRepositoryImpl
import ag.sokolov.smsrelay.data.repository.TelegramConfigPreferencesDataStoreRepository
import ag.sokolov.smsrelay.data.repository.api.TelegramBotApi
import ag.sokolov.smsrelay.data.sources.remote.telegram_bot_api.RetrofitTelegramBotApi
import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository
import ag.sokolov.smsrelay.domain.repository.TelegramConfigRepository
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
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
    abstract fun bindTelegramConfigRepository(impl: TelegramConfigPreferencesDataStoreRepository): TelegramConfigRepository

    @Binds
    abstract fun bindTelegramBotApiRepository(impl: TelegramBotApiRepositoryImpl): TelegramBotApiRepository

    companion object {
        @Singleton
        @Provides
        fun provideApplicationConfigurationPreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> =
            PreferenceDataStoreFactory.create {
                appContext.preferencesDataStoreFile("application_configuration")
            }

        @Singleton
        @Provides
        fun provideTelegramBotApi(): TelegramBotApi {
            val json = Json { ignoreUnknownKeys = true }
            val jsonMediaType = "application/json".toMediaType()

            return Retrofit.Builder()
                .baseUrl("https://api.telegram.org/")
                .addConverterFactory(json.asConverterFactory(jsonMediaType)).build()
                .create(RetrofitTelegramBotApi::class.java)
        }
    }
}
