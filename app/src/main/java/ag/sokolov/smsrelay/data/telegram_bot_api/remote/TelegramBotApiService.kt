package ag.sokolov.smsrelay.data.telegram_bot_api.remote

import ag.sokolov.smsrelay.data.telegram_bot_api.remote.dto.TelegramBotApiResponseDto
import ag.sokolov.smsrelay.data.telegram_bot_api.remote.dto.TelegramBotApiUpdateDto
import ag.sokolov.smsrelay.data.telegram_bot_api.remote.dto.TelegramMessageDto
import ag.sokolov.smsrelay.data.telegram_bot_api.remote.dto.TelegramUserDto

interface TelegramBotApiService {
    suspend fun getMe(
        token: String
    ): TelegramBotApiResponseDto<TelegramUserDto>

    suspend fun getChat(
        token: String,
        chatId: Long
    ): TelegramBotApiResponseDto<TelegramUserDto>

    suspend fun getUpdates(
        token: String,
        timeout: Long? = null,
        offset: Long? = null,
        allowedUpdates: List<String>? = null
    ): TelegramBotApiResponseDto<List<TelegramBotApiUpdateDto>>

    suspend fun sendMessage(
        token: String,
        chatId: Long,
        text: String,
    ): TelegramBotApiResponseDto<TelegramMessageDto>
}
