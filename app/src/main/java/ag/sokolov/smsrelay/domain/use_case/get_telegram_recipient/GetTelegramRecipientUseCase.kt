package ag.sokolov.smsrelay.domain.use_case.get_telegram_recipient

import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramUser
import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetTelegramRecipientUseCase
@Inject
constructor(
    private val configurationRepository: ConfigurationRepository,
    private val telegramBotApiRepository: TelegramBotApiRepository
) {
    operator fun invoke(): Flow<Response<TelegramUser?, DomainError>> =
        combine(
            configurationRepository.getTelegramBotApiToken(),
            configurationRepository.getTelegramRecipientId()) { botApiToken, recipientId ->
                if (botApiToken == null || recipientId == null) {
                    Response.Success(null)
                } else {
                    telegramBotApiRepository.getTelegramRecipient(botApiToken, recipientId)
                }
            }
}
