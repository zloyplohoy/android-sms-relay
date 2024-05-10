package ag.sokolov.smsrelay.domain.use_cases.get_telegram_bot_info

import ag.sokolov.smsrelay.domain.errors.DomainException
import ag.sokolov.smsrelay.domain.models.TelegramBotInfo
import ag.sokolov.smsrelay.domain.repositories.TelegramBotApiRepository
import ag.sokolov.smsrelay.domain.repositories.TelegramConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTelegramBotInfoUseCase @Inject constructor(
    private val telegramConfigRepository: TelegramConfigRepository,
    private val telegramBotApiRepository: TelegramBotApiRepository
) {
    operator fun invoke(): Flow<Result<TelegramBotInfo>> =
        telegramConfigRepository.getBotApiTokenFlow().map { telegramBotApiToken ->
            telegramBotApiToken?.let {
                telegramBotApiRepository.getBotInfo(it)
            } ?: Result.failure(DomainException.BotNotConfiguredException())
        }
}
