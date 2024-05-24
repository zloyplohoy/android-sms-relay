package ag.sokolov.smsrelay

import ag.sokolov.smsrelay.data.repository.PreferencesDataStoreConfigurationRepository
import ag.sokolov.smsrelay.data.repository.TelegramBotApiRepositoryImpl
import ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.TelegramBotApiService
import ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.retrofit.RetrofitTelegramBotApiService
import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository
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
  abstract fun bindConfigurationRepository(
      impl: PreferencesDataStoreConfigurationRepository
  ): ConfigurationRepository

  @Binds
  abstract fun bindTelegramBotApiRepository(
      impl: TelegramBotApiRepositoryImpl
  ): TelegramBotApiRepository

  companion object {
    @Provides
    @Singleton
    fun provideApplicationConfigurationPreferencesDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create {
          appContext.preferencesDataStoreFile("application_configuration")
        }

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext appContext: Context): Context = appContext

    @Provides
    @Singleton
    fun provideTelegramBotApi2(): TelegramBotApiService {
      val json = Json { ignoreUnknownKeys = true }
      val jsonMediaType = "application/json".toMediaType()

      return Retrofit.Builder()
          .baseUrl("https://api.telegram.org/")
          .addConverterFactory(json.asConverterFactory(jsonMediaType))
          .build()
          .create(RetrofitTelegramBotApiService::class.java)
    }
  }
}
