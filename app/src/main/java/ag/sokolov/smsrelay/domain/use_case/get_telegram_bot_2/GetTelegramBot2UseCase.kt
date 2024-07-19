package ag.sokolov.smsrelay.domain.use_case.get_telegram_bot_2

import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import ag.sokolov.smsrelay.data.telegram_bot_api.TelegramBotApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTelegramBot2UseCase
@Inject constructor(
    private val configurationRepository: ConfigurationRepository,
    private val telegramBotApi: TelegramBotApi
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Response<TelegramBot?, DomainError>> =
        configurationRepository.getTelegramBotApiToken().flatMapLatest { telegramApiToken ->
            flow {
                telegramApiToken?.let {
                    emit(Response.Loading)
                    emit(telegramBotApi.getTelegramBot(it))
                } ?: emit(Response.Success(null))
            }
        }
}
