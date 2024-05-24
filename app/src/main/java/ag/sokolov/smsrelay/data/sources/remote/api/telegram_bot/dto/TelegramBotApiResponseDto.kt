package ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.dto

import kotlinx.serialization.Serializable

// https://core.telegram.org/bots/api#making-requests

@Serializable data class TelegramBotApiResponseDto<T>(val ok: Boolean, val result: T)
