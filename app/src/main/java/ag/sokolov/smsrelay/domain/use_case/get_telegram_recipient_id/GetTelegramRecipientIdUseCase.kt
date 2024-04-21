package ag.sokolov.smsrelay.domain.use_case.get_telegram_recipient_id

import ag.sokolov.smsrelay.domain.repository.TelegramConfigRepository
import javax.inject.Inject

class GetTelegramRecipientIdUseCase @Inject constructor(
    private val telegramConfigRepository: TelegramConfigRepository
) {
    suspend operator fun invoke(): Result<Long> = telegramConfigRepository.getRecipientId()
}
