package ag.sokolov.smsrelay.data.sources.remote.apis.telegram_bot

import ag.sokolov.smsrelay.data.sources.remote.apis.telegram_bot.dtos.TelegramBotApiResponseDto
import ag.sokolov.smsrelay.data.sources.remote.apis.telegram_bot.dtos.TelegramUserDto
import ag.sokolov.smsrelay.data.sources.remote.apis.telegram_bot.dtos.TelegramMessageDto
import ag.sokolov.smsrelay.data.sources.remote.apis.telegram_bot.dtos.TelegramBotApiUpdateDto
import retrofit2.Response

interface TelegramBotApiService {
    suspend fun getMe(
        token: String
    ): Response<TelegramBotApiResponseDto<TelegramUserDto>>

    suspend fun getUpdates(
        token: String,
        timeout: Long? = null,
        allowedUpdates: List<String>? = null
    ): Response<TelegramBotApiResponseDto<List<TelegramBotApiUpdateDto>>>

    suspend fun sendMessage(
        token: String,
        chatId: Long,
        text: String,
    ): Response<TelegramBotApiResponseDto<TelegramMessageDto>>

    suspend fun getChat(
        token: String,
        chatId: Long
    ): Response<TelegramBotApiResponseDto<TelegramUserDto>>
}
