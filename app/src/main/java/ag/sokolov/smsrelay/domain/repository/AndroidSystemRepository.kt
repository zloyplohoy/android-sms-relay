package ag.sokolov.smsrelay.domain.repository

import kotlinx.coroutines.flow.Flow

interface AndroidSystemRepository {
    fun getConnectionStatus(): Flow<Boolean>
}
