package ag.sokolov.smsrelay.data.repository.api

import ag.sokolov.smsrelay.data.repository.api.dto.TelegramBotApiResponseDto
import ag.sokolov.smsrelay.data.repository.api.dto.TelegramBotApiUserDto
import ag.sokolov.smsrelay.data.repository.api.dto.TelegramMessageDto
import ag.sokolov.smsrelay.data.repository.api.dto.TelegramUpdateDto
import retrofit2.Response

interface TelegramBotApi {
    suspend fun getMe(token: String): Response<TelegramBotApiResponseDto<TelegramBotApiUserDto>>
    suspend fun getUpdates(
        token: String, timeout: Long?, allowedUpdates: List<String>?
    ): Response<TelegramBotApiResponseDto<List<TelegramUpdateDto>>>

    suspend fun sendMessage(
        token: String, text: String, chatId: Long
    ): Response<TelegramBotApiResponseDto<TelegramMessageDto>>
}
