package ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.retrofit

import ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.TelegramBotApiService
import ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.dto.TelegramBotApiResponseDto
import ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.dto.TelegramBotApiUpdateDto
import ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.dto.TelegramMessageDto
import ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.dto.TelegramUserDto
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitTelegramBotApiService : TelegramBotApiService {
    // https://core.telegram.org/bots/api#getme
    @GET("/bot{token}/getMe")
    override suspend fun getMe(
        @Path("token") token: String
    ): Response<TelegramBotApiResponseDto<TelegramUserDto>>

    // https://core.telegram.org/bots/api#getupdates
    @GET("/bot{token}/getUpdates")
    override suspend fun getUpdates(
        @Path("token") token: String,
        @Query("timeout") timeout: Long?,
        @Query("allowed_updates") allowedUpdates: List<String>?
    ): Response<TelegramBotApiResponseDto<List<TelegramBotApiUpdateDto>>>

    // https://core.telegram.org/bots/api#sendmessage
    @FormUrlEncoded
    @POST("/bot{token}/sendMessage")
    override suspend fun sendMessage(
        @Path("token") token: String, @Field("chat_id") chatId: Long, @Field("text") text: String
    ): Response<TelegramBotApiResponseDto<TelegramMessageDto>>

    // https://core.telegram.org/bots/api#getchat
    @GET("/bot{token}/getChat")
    override suspend fun getChat(
        @Path("token") token: String,
        @Query("chat_id") chatId: Long
    ): Response<TelegramBotApiResponseDto<TelegramUserDto>>
}
