package ag.sokolov.smsrelay.domain.use_case.remove_telegram_bot

import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import javax.inject.Inject

class RemoveTelegramBotUseCase
@Inject
constructor(private val configurationRepository: ConfigurationRepository) {
    suspend operator fun invoke(): Result<Unit> =
        configurationRepository.deleteTelegramBotApiToken()
}