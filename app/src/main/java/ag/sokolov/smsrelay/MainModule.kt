package ag.sokolov.smsrelay

import ag.sokolov.smsrelay.data.repository.AndroidSystemRepositoryImpl
import ag.sokolov.smsrelay.domain.repository.AndroidSystemRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MainModule {

    @Binds
    abstract fun bindAndroidSystemRepository(
        impl: AndroidSystemRepositoryImpl
    ): AndroidSystemRepository
}
