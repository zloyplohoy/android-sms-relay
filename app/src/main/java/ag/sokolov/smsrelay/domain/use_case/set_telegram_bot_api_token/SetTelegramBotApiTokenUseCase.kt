package ag.sokolov.smsrelay.domain.use_case.set_telegram_bot_api_token

import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository
import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository.Companion.isTelegramBotApiTokenStructureValid
import ag.sokolov.smsrelay.domain.repository.TelegramConfigRepository
import javax.inject.Inject

class SetTelegramBotApiTokenUseCase @Inject constructor(
    private val telegramConfigRepository: TelegramConfigRepository,
    private val telegramBotApiRepository: TelegramBotApiRepository
) {
    suspend operator fun invoke(botApiToken: String): Result<Unit> = runCatching {
        require(isTelegramBotApiTokenStructureValid(botApiToken)) { "Invalid token format" }
        require(isTelegramBotApiTokenValid(botApiToken)) { "Invalid token" }
        telegramConfigRepository.setBotApiToken(botApiToken).map { }
    }

    private suspend fun isTelegramBotApiTokenValid(botApiToken: String) =
        telegramBotApiRepository.getBotDetails(botApiToken).isSuccess
}
