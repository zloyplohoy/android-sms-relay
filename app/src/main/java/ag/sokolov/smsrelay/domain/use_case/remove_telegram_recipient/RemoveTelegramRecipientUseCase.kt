package ag.sokolov.smsrelay.domain.use_case.remove_telegram_recipient

import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import javax.inject.Inject

class RemoveTelegramRecipientUseCase @Inject constructor(
    private val configurationRepository: ConfigurationRepository
) {
    suspend operator fun invoke(): Result<Unit> = runCatching {
        configurationRepository.deleteTelegramRecipientId().getOrThrow()
        configurationRepository.deleteCachedTelegramRecipient().getOrThrow()
    }
}
