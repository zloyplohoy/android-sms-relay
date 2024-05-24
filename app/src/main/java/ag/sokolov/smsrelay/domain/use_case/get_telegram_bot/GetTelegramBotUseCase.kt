package ag.sokolov.smsrelay.domain.use_case.get_telegram_bot

import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTelegramBotUseCase
@Inject
constructor(
    private val configurationRepository: ConfigurationRepository,
    private val telegramBotApiRepository: TelegramBotApiRepository
) {
    operator fun invoke(): Flow<Response<TelegramBot, DomainError>> =
        configurationRepository.getTelegramBotApiToken().map { telegramBotApiToken ->
            telegramBotApiToken?.let { telegramBotApiRepository.getTelegramBot(it) }
                ?: Response.Failure(DomainError.BotApiTokenMissing)
        }
}
