package ag.sokolov.smsrelay.domain.use_case.delete_telegram_bot_api_token

import ag.sokolov.smsrelay.domain.repository.TelegramConfigRepository
import javax.inject.Inject

class DeleteTelegramBotApiTokenUseCase @Inject constructor(
    private val telegramConfigRepository: TelegramConfigRepository
) {
    suspend operator fun invoke(): Result<Unit> = telegramConfigRepository.deleteBotApiToken()
}
