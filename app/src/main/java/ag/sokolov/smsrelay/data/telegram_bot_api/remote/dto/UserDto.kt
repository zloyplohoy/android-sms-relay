package ag.sokolov.smsrelay.data.telegram_bot_api.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://core.telegram.org/bots/api#user

@Serializable
data class UserDto(
    val id: Long,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String? = null,
    val username: String? = null,
)
