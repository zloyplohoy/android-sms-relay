package ag.sokolov.smsrelay.domain.use_case.get_telegram_bot

import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetTelegramBotUseCase @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
    private val telegramBotApiRepository: TelegramBotApiRepository
) {
    operator fun invoke(): Flow<Result<TelegramBot?>> =
        configurationRepository.getTelegramBotApiToken().map { telegramBotApiToken ->
            telegramBotApiToken?.let {
                telegramBotApiRepository.getTelegramBot(it)
            } ?: Result.success(null)
        }.onEach { telegramBotResult ->
            telegramBotResult.getOrNull()?.let { telegramBot ->
                configurationRepository.setCachedTelegramBot(telegramBot)
            }
        }.onStart {
            emit(Result.success(configurationRepository.getCachedTelegramBot().first()))
        }.distinctUntilChanged()
}
