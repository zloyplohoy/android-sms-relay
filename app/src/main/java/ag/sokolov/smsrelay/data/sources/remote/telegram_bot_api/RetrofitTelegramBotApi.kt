package ag.sokolov.smsrelay.data.sources.remote.telegram_bot_api

import ag.sokolov.smsrelay.data.repository.api.TelegramBotApi
import ag.sokolov.smsrelay.data.repository.api.dto.TelegramBotApiResponseDto
import ag.sokolov.smsrelay.data.repository.api.dto.TelegramBotApiUserDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitTelegramBotApi : TelegramBotApi {
    @GET("/bot{token}/getMe")
    override suspend fun getMe(
        @Path("token") token: String
    ): Response<TelegramBotApiResponseDto<TelegramBotApiUserDto>>
}