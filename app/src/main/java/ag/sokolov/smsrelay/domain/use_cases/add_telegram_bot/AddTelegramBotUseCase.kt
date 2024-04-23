package ag.sokolov.smsrelay.domain.use_cases.add_telegram_bot

import ag.sokolov.smsrelay.domain.repositories.TelegramConfigRepository
import javax.inject.Inject

class AddTelegramBotUseCase @Inject constructor(
    private val telegramConfigRepository: TelegramConfigRepository
) {
    suspend operator fun invoke(botApiToken: String): Result<Unit> =
        // TODO: Handle (map) repository errors
        telegramConfigRepository.setBotApiToken(botApiToken)
}
