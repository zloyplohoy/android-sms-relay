package ag.sokolov.smsrelay.domain.use_case.get_telegram_bot_api_token_flow

import ag.sokolov.smsrelay.domain.repository.TelegramConfigRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTelegramBotApiTokenFlowUseCase @Inject constructor(
    private val telegramConfigRepository: TelegramConfigRepository
) {
    operator fun invoke(): Flow<String?> = telegramConfigRepository.getBotApiTokenFlow()
}
