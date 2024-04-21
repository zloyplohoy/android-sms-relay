package ag.sokolov.smsrelay.data.repository.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TelegramUpdateDto(
    // https://core.telegram.org/bots/api#update
    @SerialName("update_id") val updateId: Long, val message: TelegramMessageDto?
)