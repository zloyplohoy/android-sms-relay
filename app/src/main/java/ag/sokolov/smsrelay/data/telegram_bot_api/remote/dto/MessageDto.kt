package ag.sokolov.smsrelay.data.telegram_bot_api.remote.dto

import kotlinx.serialization.Serializable

// https://core.telegram.org/bots/api#message

@Serializable
data class MessageDto(
    val from: UserDto?,
    val text: String?
)
