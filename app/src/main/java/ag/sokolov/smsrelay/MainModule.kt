package ag.sokolov.smsrelay

import ag.sokolov.smsrelay.data.repository.TelegramConfigPreferencesDataStoreRepository
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MainModule {

    @Binds
    abstract fun bindTelegramConfigRepository(impl: TelegramConfigPreferencesDataStoreRepository): TelegramConfigRepository

    companion object {
        @Singleton
        @Provides
        fun provideApplicationConfigurationPreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> =
            PreferenceDataStoreFactory.create {
                appContext.preferencesDataStoreFile("application_configuration")
            }
    }
}
