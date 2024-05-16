package ag.sokolov.smsrelay.domain.use_cases.get_telegram_recipient

import ag.sokolov.smsrelay.domain.errors.DomainException
import ag.sokolov.smsrelay.domain.models.TelegramUser
import ag.sokolov.smsrelay.domain.repositories.ConfigurationRepository
import ag.sokolov.smsrelay.domain.repositories.TelegramBotApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetTelegramRecipientUseCase @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
    private val telegramBotApiRepository: TelegramBotApiRepository
) {
    operator fun invoke(): Flow<Result<TelegramUser?>> =
        configurationRepository.getTelegramRecipientId()
            .combine(configurationRepository.getTelegramBotApiToken()) { recipientId, botApiToken ->
                runCatching {
                    validateBotApiToken(botApiToken)
                    recipientId?.let {
                        telegramBotApiRepository.getChat(it, botApiToken!!).getOrThrow()
                    }
                }
            }.onEach { telegramRecipientResult ->
                telegramRecipientResult.getOrNull()?.let { recipient ->
                    configurationRepository.setCachedTelegramRecipient(recipient)
                }
            }.onStart {
                emit(Result.success(configurationRepository.getCachedTelegramRecipient().first()))
            }.distinctUntilChanged()

    private suspend fun validateBotApiToken(token: String?) {
        if (token == null) throw DomainException.BotNotConfiguredException()
        if (isBotApiTokenInvalid(token)) throw DomainException.InvalidBotApiTokenException()
    }

    private suspend fun isBotApiTokenInvalid(token: String) =
        telegramBotApiRepository.getTelegramBot(token).isFailure
}
