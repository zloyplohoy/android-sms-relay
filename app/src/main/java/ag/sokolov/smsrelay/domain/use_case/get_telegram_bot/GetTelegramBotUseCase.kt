package ag.sokolov.smsrelay.domain.use_case.get_telegram_bot

import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.repository.AndroidSystemRepository
import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTelegramBotUseCase
@Inject
constructor(
    private val androidSystemRepository: AndroidSystemRepository,
    private val configurationRepository: ConfigurationRepository,
    private val telegramBotApiRepository: TelegramBotApiRepository
) {
    operator fun invoke(): Flow<Response<TelegramBot?, DomainError>> =
        combine(
            androidSystemRepository.getConnectionStatus(),
            configurationRepository.getTelegramBotApiToken()
        ) { isOnline, telegramBotApiToken ->
            if (isOnline) {
                telegramBotApiToken?.let { telegramBotApiRepository.getTelegramBot(it) }
                ?: Response.Success(null)
            } else {
                Response.Failure(DomainError.NetworkUnavailable)
            }
        }
}
