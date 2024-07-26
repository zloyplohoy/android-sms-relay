package ag.sokolov.smsrelay.data.telegram_bot_api.retrofit.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://core.telegram.org/bots/api#update

@Serializable
data class UpdateDto(
    @SerialName("update_id") val updateId: Long,
    val message: MessageDto? = null
)
