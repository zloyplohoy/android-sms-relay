package ag.sokolov.smsrelay.data.sources.remote.telegram_bot_api

import ag.sokolov.smsrelay.data.repository.api.TelegramBotApi
import ag.sokolov.smsrelay.data.repository.api.dto.TelegramBotApiResponseDto
import ag.sokolov.smsrelay.data.repository.api.dto.TelegramBotApiUserDto
import ag.sokolov.smsrelay.data.repository.api.dto.TelegramMessageDto
import ag.sokolov.smsrelay.data.repository.api.dto.TelegramUpdateDto
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitTelegramBotApi : TelegramBotApi {
    @GET("/bot{token}/getMe")
    override suspend fun getMe(
        @Path("token") token: String
    ): Response<TelegramBotApiResponseDto<TelegramBotApiUserDto>>

    // https://core.telegram.org/bots/api#getupdates
    @GET("/bot{token}/getUpdates")
    override suspend fun getUpdates(
        @Path("token") token: String,
        @Query("timeout") timeout: Long?,
        @Query("allowed_updates") allowedUpdates: List<String>?
    ): Response<TelegramBotApiResponseDto<List<TelegramUpdateDto>>>

    // https://core.telegram.org/bots/api#sendmessage
    @FormUrlEncoded
    @POST("/bot{token}/sendMessage")
    override suspend fun sendMessage(
        @Path("token") token: String, @Field("text") text: String, @Field("chat_id") chatId: Long
    ): Response<TelegramBotApiResponseDto<TelegramMessageDto>>
}
