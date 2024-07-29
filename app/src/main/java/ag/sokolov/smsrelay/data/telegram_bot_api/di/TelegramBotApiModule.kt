package ag.sokolov.smsrelay.data.telegram_bot_api.di

import ag.sokolov.smsrelay.data.telegram_bot_api.TelegramBotApi2
import ag.sokolov.smsrelay.data.telegram_bot_api.TelegramBotApiImpl2
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TelegramBotApiModule {

    @Binds
    internal abstract fun bindTelegramBotApiRepository2(impl: TelegramBotApiImpl2): TelegramBotApi2

    companion object {

        @Provides
        @Singleton
        fun provideOkHttpCallFactory(): Call.Factory {
            return OkHttpClient.Builder().build()
        }
    }
}
