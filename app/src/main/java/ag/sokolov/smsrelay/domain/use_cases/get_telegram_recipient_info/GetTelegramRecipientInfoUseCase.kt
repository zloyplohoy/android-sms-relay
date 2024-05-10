package ag.sokolov.smsrelay.domain.use_cases.get_telegram_recipient_info

import ag.sokolov.smsrelay.domain.models.TelegramBotInfo
import ag.sokolov.smsrelay.domain.repositories.TelegramBotApiRepository
import ag.sokolov.smsrelay.domain.repositories.TelegramConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject

//class GetTelegramRecipientInfoUseCase @Inject constructor(
//    private val telegramConfigRepository: TelegramConfigRepository,
//    private val telegramBotApiRepository: TelegramBotApiRepository
//) {
//    operator fun invoke(): Flow<Result<TelegramBotInfo>> =
//        // TODO: Combine two flows somehow
//        telegramConfigRepository.getRecipientIdFlow().map { telegramRecipientId ->
//            telegramRecipientId?.let {
//                telegramBotApiRepository.getChat(telegramRecipientId).onSuccess {
//                    Result.success(TelegramBotInfo())
//                }.onFailure { Result.failure<IOException>(IOException("fsdd")) }
//            } ?: Result.success(TelegramBotInfo())
//        }
//}
