package ag.sokolov.smsrelay.core.data.di

import ag.sokolov.smsrelay.core.data.repository.TelegramConfigRepository
import ag.sokolov.smsrelay.core.data.repository.TelegramConfigRepositoryImpl
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
