package ag.sokolov.smsrelay.data.telegram_bot_api.remote

import ag.sokolov.smsrelay.data.telegram_bot_api.remote.dto.TelegramBotApiResponseDto
import ag.sokolov.smsrelay.data.telegram_bot_api.remote.dto.TelegramBotApiUpdateDto
import ag.sokolov.smsrelay.data.telegram_bot_api.remote.dto.TelegramMessageDto
import ag.sokolov.smsrelay.data.telegram_bot_api.remote.dto.TelegramUserDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TelegramBotApiServiceImpl : TelegramBotApiService {
    // https://core.telegram.org/bots/api#getme
    @GET("/bot{token}/getMe")
    override suspend fun getMe(
        @Path("token") token: String
    ): TelegramBotApiResponseDto<TelegramUserDto>

    // https://core.telegram.org/bots/api#getupdates
    @GET("/bot{token}/getUpdates")
    override suspend fun getUpdates(
        @Path("token") token: String,
        @Query("timeout") timeout: Long?,
        @Query("allowed_updates") allowedUpdates: List<String>?
    ): TelegramBotApiResponseDto<List<TelegramBotApiUpdateDto>>

    // https://core.telegram.org/bots/api#sendmessage
    @FormUrlEncoded
    @POST("/bot{token}/sendMessage")
    override suspend fun sendMessage(
        @Path("token") token: String,
        @Field("chat_id") chatId: Long,
        @Field("text") text: String
    ): TelegramBotApiResponseDto<TelegramMessageDto>

    // https://core.telegram.org/bots/api#getchat
    @GET("/bot{token}/getChat")
    override suspend fun getChat(
        @Path("token") token: String,
        @Query("chat_id") chatId: Long
    ): TelegramBotApiResponseDto<TelegramUserDto>
}