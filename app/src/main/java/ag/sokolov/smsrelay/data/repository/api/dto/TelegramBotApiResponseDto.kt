package ag.sokolov.smsrelay.data.repository.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class TelegramBotApiResponseDto<T>(
    val ok: Boolean,
    val result: T,
)
