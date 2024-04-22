package ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.dto;

import kotlinx.serialization.SerialName;
import kotlinx.serialization.Serializable;

// https://core.telegram.org/bots/api#message

@Serializable
data class TelegramMessageDto(
    @SerialName("message_id") val messageId: Long,
    val from: TelegramUserDto?,
    val date: Long,
    val text: String?
)