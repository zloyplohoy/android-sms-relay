package ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot

import ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.dto.TelegramBotApiResponseDto
import ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.dto.TelegramBotApiUpdateDto
import ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.dto.TelegramMessageDto
import ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.dto.TelegramUserDto

interface TelegramBotApiService {
    suspend fun getMe(token: String): TelegramBotApiResponseDto<TelegramUserDto>

    suspend fun getChat(token: String, chatId: Long): TelegramBotApiResponseDto<TelegramUserDto>

    suspend fun getUpdates(
        token: String,
        timeout: Long? = null,
        allowedUpdates: List<String>? = null
    ): TelegramBotApiResponseDto<List<TelegramBotApiUpdateDto>>

    suspend fun sendMessage(
        token: String,
        chatId: Long,
        text: String,
    ): TelegramBotApiResponseDto<TelegramMessageDto>
}
