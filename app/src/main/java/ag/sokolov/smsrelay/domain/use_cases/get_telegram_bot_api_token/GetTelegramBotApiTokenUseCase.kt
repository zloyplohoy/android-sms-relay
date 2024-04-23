package ag.sokolov.smsrelay.domain.use_cases.get_telegram_bot_api_token

import ag.sokolov.smsrelay.domain.repositories.TelegramConfigRepository
import javax.inject.Inject

class GetTelegramBotApiTokenUseCase @Inject constructor(
    private val telegramConfigRepository: TelegramConfigRepository
) {
    suspend operator fun invoke(): Result<String> = telegramConfigRepository.getBotApiToken()
}
