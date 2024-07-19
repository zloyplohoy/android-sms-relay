package ag.sokolov.smsrelay.data.telegram_bot_api.remote.di

import ag.sokolov.smsrelay.data.telegram_bot_api.remote.TelegramBotApiService
import ag.sokolov.smsrelay.data.telegram_bot_api.remote.TelegramBotApiServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object TelegramBotApiServiceModule {
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
