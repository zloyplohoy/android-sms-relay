package ag.sokolov.smsrelay.domain.use_case.is_online

import ag.sokolov.smsrelay.domain.repository.AndroidSystemRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class IsOnlineUseCase
@Inject
constructor(private val androidSystemRepository: AndroidSystemRepository) {
    operator fun invoke(): Flow<Boolean> = androidSystemRepository.getConnectionStatus()
}
