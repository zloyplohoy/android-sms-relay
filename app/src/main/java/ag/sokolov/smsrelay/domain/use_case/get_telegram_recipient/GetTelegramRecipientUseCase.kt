package ag.sokolov.smsrelay.domain.use_case.get_telegram_recipient

import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramUser
import ag.sokolov.smsrelay.domain.repository.AndroidSystemRepository
import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetTelegramRecipientUseCase
@Inject constructor(
    private val androidSystemRepository: AndroidSystemRepository,
    private val configurationRepository: ConfigurationRepository,
    private val telegramBotApiRepository: TelegramBotApiRepository
) {
    operator fun invoke(): Flow<Response<TelegramUser?, DomainError>> =
        combine(
            androidSystemRepository.getConnectionStatus(),
            configurationRepository.getTelegramBotApiToken(),
            configurationRepository.getTelegramRecipientId()
        ) { isOnline, botApiToken, recipientId ->
            if (isOnline) {
                if (botApiToken == null) {
                    Response.Failure(DomainError.BotApiTokenMissing)
                } else if (recipientId == null) {
                    when (val result = telegramBotApiRepository.getTelegramBot(botApiToken)) {
                        is Response.Loading -> Response.Loading
                        is Response.Success -> Response.Success(null)
                        is Response.Failure -> Response.Failure(result.error)
                    }
                } else {
                    telegramBotApiRepository.getTelegramRecipient(botApiToken, recipientId)
                }
            } else {
                Response.Failure(DomainError.NetworkUnavailable)
            }
        }
}
