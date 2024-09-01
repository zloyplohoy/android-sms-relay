package ag.sokolov.smsrelay.data.database.di

import ag.sokolov.smsrelay.data.database.SMSRelayDatabase
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): SMSRelayDatabase =
        Room.databaseBuilder(
            context,
            SMSRelayDatabase::class.java,
            "sms_relay_database"
        ).build()
}
