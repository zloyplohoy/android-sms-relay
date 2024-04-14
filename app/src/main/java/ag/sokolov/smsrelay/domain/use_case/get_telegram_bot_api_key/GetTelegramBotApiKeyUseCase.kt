package ag.sokolov.smsrelay.domain.use_case.get_telegram_bot_api_key

import ag.sokolov.smsrelay.domain.repository.TelegramConfigRepository
import javax.inject.Inject

class GetTelegramBotApiKeyUseCase @Inject constructor(
    private val telegramConfigRepository: TelegramConfigRepository
) {
    suspend operator fun invoke(): Result<String?> = telegramConfigRepository.getBotApiToken()
}
