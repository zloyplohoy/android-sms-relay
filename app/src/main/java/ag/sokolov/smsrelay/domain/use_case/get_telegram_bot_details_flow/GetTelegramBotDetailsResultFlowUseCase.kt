package ag.sokolov.smsrelay.domain.use_case.get_telegram_bot_details_flow

import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository
import ag.sokolov.smsrelay.domain.repository.TelegramConfigRepository
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
