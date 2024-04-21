package ag.sokolov.smsrelay.data.repository.api.dto;

import kotlinx.serialization.SerialName;
import kotlinx.serialization.Serializable;

@Serializable
data class TelegramMessageDto(
    // https://core.telegram.org/bots/api#message
    @SerialName("message_id") val messageId: Long,
    val from: TelegramBotApiUserDto?,
    val date: Long,
    val text: String?
)
