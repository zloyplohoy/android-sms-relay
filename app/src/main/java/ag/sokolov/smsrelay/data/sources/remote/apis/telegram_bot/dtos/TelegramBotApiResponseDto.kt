package ag.sokolov.smsrelay.data.sources.remote.apis.telegram_bot.dtos

import kotlinx.serialization.Serializable

// https://core.telegram.org/bots/api#making-requests

@Serializable
data class TelegramBotApiResponseDto<T>(
    val ok: Boolean,
    val result: T
)
