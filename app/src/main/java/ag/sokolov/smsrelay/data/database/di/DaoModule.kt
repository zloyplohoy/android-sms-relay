package ag.sokolov.smsrelay.data.database.di

import ag.sokolov.smsrelay.data.database.SMSRelayDatabase
import ag.sokolov.smsrelay.data.database.dao.RecipientDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun provideRecipientDao(
        database: SMSRelayDatabase
    ): RecipientDao =
        database.recipientDao()
}
