package ag.sokolov.smsrelay.domain.use_cases.set_telegram_bot_api_token

import ag.sokolov.smsrelay.domain.repositories.TelegramConfigRepository
import java.io.IOException
import javax.inject.Inject

class SetTelegramBotApiTokenUseCase @Inject constructor(
    private val telegramConfigRepository: TelegramConfigRepository
) {
    suspend operator fun invoke(botApiToken: String): Result<Unit> = runCatching {
        telegramConfigRepository.setBotApiToken(botApiToken).onFailure {
            throw IOException("Token could not be saved")
        }
    }
}
