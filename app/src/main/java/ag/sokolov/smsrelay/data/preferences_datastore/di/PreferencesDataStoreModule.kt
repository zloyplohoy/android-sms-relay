package ag.sokolov.smsrelay.data.preferences_datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesDataStoreModule {

    private const val DATA_STORE_NAME = "preferences_data_store"
    private val CORRUPTION_HANDLER = ReplaceFileCorruptionHandler { emptyPreferences() }

    private val Context.preferencesDataStore by preferencesDataStore(
        name = DATA_STORE_NAME,
        corruptionHandler = CORRUPTION_HANDLER
    )

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<Preferences> =
        appContext.preferencesDataStore
}
