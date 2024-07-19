package ag.sokolov.smsrelay.data.telegram_bot_api.remote.dto

import kotlinx.serialization.Serializable

// https://core.telegram.org/bots/api#making-requests

@Serializable
data class TelegramBotApiResponseDto<T>(
    val ok: Boolean,
    val result: T
)
