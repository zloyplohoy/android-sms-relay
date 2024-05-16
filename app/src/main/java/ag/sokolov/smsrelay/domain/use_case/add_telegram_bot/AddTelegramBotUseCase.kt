package ag.sokolov.smsrelay.domain.use_case.add_telegram_bot

import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import javax.inject.Inject

class AddTelegramBotUseCase @Inject constructor(
    private val configurationRepository: ConfigurationRepository
) {
    suspend operator fun invoke(botApiToken: String): Result<Unit> =
        // TODO: Handle (map) repository errors
        configurationRepository.setTelegramBotApiToken(botApiToken)
}
