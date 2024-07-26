package ag.sokolov.smsrelay.data.telegram_bot_api.remote

import ag.sokolov.smsrelay.data.telegram_bot_api.remote.dto.ResponseDto
import ag.sokolov.smsrelay.data.telegram_bot_api.remote.dto.UpdateDto
import ag.sokolov.smsrelay.data.telegram_bot_api.remote.dto.MessageDto
import ag.sokolov.smsrelay.data.telegram_bot_api.remote.dto.UserDto

interface TelegramBotApiService {
    suspend fun getMe(
        token: String
    ): ResponseDto<UserDto>

    suspend fun getChat(
        token: String,
        chatId: Long
    ): ResponseDto<UserDto>

    suspend fun getUpdates(
        token: String,
        timeout: Long? = null,
        offset: Long? = null,
        allowedUpdates: List<String>? = null
    ): ResponseDto<List<UpdateDto>>

    suspend fun sendMessage(
        token: String,
        chatId: Long,
        text: String,
    ): ResponseDto<MessageDto>
}
