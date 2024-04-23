package ag.sokolov.smsrelay.data.sources.remote.apis.telegram_bot.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://core.telegram.org/bots/api#update

@Serializable
data class TelegramBotApiUpdateDto(
    @SerialName("update_id") val updateId: Long,
    val message: TelegramMessageDto?
)
