package ag.sokolov.smsrelay.data.telegram_bot_api.di

import ag.sokolov.smsrelay.data.telegram_bot_api.TelegramBotApi
import ag.sokolov.smsrelay.data.telegram_bot_api.TelegramBotApiImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TelegramBotApiModule {
    @Binds
    internal abstract fun bindTelegramBotApiRepository(impl: TelegramBotApiImpl): TelegramBotApi
}
