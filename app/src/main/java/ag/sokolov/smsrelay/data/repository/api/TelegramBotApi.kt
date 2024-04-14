package ag.sokolov.smsrelay.data.repository.api

import ag.sokolov.smsrelay.data.repository.api.dto.TelegramBotApiResponseDto
import ag.sokolov.smsrelay.data.repository.api.dto.TelegramBotApiUserDto
import retrofit2.Response

interface TelegramBotApi {
    suspend fun getMe(token: String): Response<TelegramBotApiResponseDto<TelegramBotApiUserDto>>
}