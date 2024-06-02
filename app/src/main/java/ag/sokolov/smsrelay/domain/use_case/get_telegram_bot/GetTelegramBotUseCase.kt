package ag.sokolov.smsrelay.domain.use_case.get_telegram_bot

import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.repository.AndroidSystemRepository
import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTelegramBotUseCase
@Inject
constructor(
    private val androidSystemRepository: AndroidSystemRepository,
    private val configurationRepository: ConfigurationRepository,
    private val telegramBotApiRepository: TelegramBotApiRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Response<TelegramBot?, DomainError>> =
        combine(
                androidSystemRepository.getConnectionStatus(),
                configurationRepository.getTelegramBotApiToken()) { isOnline, telegramBotApiToken ->
                    Pair(isOnline, telegramBotApiToken)
                }
            .flatMapLatest { (isOnline, telegramBotApiToken) ->
                flow {
                    if (isOnline) {
                        emit(Response.Loading)
                        telegramBotApiToken?.let {
                            emit(telegramBotApiRepository.getTelegramBot(it))
                        } ?: emit(Response.Success(null))
                    } else {
                        emit(Response.Failure(DomainError.NetworkUnavailable))
                    }
                }
            }
}
