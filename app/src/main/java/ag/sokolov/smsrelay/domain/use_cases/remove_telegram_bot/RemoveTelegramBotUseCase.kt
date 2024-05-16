package ag.sokolov.smsrelay.domain.use_cases.remove_telegram_bot

import ag.sokolov.smsrelay.domain.repositories.ConfigurationRepository
import javax.inject.Inject

class RemoveTelegramBotUseCase @Inject constructor(
    private val configurationRepository: ConfigurationRepository
) {
    suspend operator fun invoke(): Result<Unit> = runCatching {
        configurationRepository.deleteTelegramBotApiToken().getOrThrow()
        configurationRepository.deleteCachedTelegramBot().getOrThrow()
    }
}
