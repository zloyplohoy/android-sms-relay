package ag.sokolov.smsrelay.domain.use_cases.remove_telegram_bot

import ag.sokolov.smsrelay.domain.repositories.TelegramConfigRepository
import javax.inject.Inject

class RemoveTelegramBotUseCase @Inject constructor(
    private val telegramConfigRepository: TelegramConfigRepository
) {
    suspend operator fun invoke(): Result<Unit> =
        // TODO: Handle (map) repository errors
        telegramConfigRepository.deleteBotApiToken()
}
