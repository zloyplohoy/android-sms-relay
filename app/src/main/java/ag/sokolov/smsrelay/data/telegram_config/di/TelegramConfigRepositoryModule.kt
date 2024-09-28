package ag.sokolov.smsrelay.data.telegram_config.di

import ag.sokolov.smsrelay.data.telegram_config.TelegramConfigRepository
import ag.sokolov.smsrelay.data.telegram_config.TelegramConfigRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TelegramConfigRepositoryModule {

    @Binds
    abstract fun bindTelegramConfig(impl: TelegramConfigRepositoryImpl): TelegramConfigRepository
}
