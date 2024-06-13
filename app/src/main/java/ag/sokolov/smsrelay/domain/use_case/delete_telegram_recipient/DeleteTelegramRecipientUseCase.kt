package ag.sokolov.smsrelay.domain.use_case.delete_telegram_recipient

import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import javax.inject.Inject

class DeleteTelegramRecipientUseCase
@Inject constructor(private val configurationRepository: ConfigurationRepository) {
    suspend operator fun invoke() =
        configurationRepository.deleteTelegramRecipientId()
}
