package ag.sokolov.smsrelay.data.telegram_config.di

import ag.sokolov.smsrelay.data.telegram_config.TelegramConfig
import ag.sokolov.smsrelay.data.telegram_config.TelegramConfigImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TelegramConfigModule {

    @Binds
    abstract fun bindTelegramConfig(impl: TelegramConfigImpl): TelegramConfig
}
