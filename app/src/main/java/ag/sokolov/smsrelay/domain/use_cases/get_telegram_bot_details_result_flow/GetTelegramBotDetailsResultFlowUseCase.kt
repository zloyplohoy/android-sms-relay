package ag.sokolov.smsrelay.domain.use_cases.get_telegram_bot_details_result_flow

import ag.sokolov.smsrelay.domain.models.TelegramBot
import ag.sokolov.smsrelay.domain.repositories.TelegramBotApiRepository
import ag.sokolov.smsrelay.domain.repositories.TelegramConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTelegramBotDetailsResultFlowUseCase @Inject constructor(
    private val telegramConfigRepository: TelegramConfigRepository,
    private val telegramBotApiRepository: TelegramBotApiRepository
) {
    operator fun invoke(): Flow<Result<TelegramBot>> =
        telegramConfigRepository.getBotApiTokenFlow().map { telegramBotApiToken ->
            telegramBotApiToken?.let {
                telegramBotApiRepository.getBotDetails(telegramBotApiToken)
            } ?: Result.failure(IllegalArgumentException("Telegram bot API token not set"))
        }
}
