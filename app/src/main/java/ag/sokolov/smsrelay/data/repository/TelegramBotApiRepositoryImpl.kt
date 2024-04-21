package ag.sokolov.smsrelay.data.repository

import ag.sokolov.smsrelay.data.repository.api.TelegramBotApi
import ag.sokolov.smsrelay.data.repository.api.dto.TelegramBotApiUserDto
import ag.sokolov.smsrelay.data.repository.api.dto.TelegramMessageDto
import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.model.TelegramPrivateChatMessage
import ag.sokolov.smsrelay.domain.model.TelegramUser
import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository
import java.io.IOException
import javax.inject.Inject
import kotlin.time.Duration


class TelegramBotApiRepositoryImpl @Inject constructor(
    private val telegramBotApi: TelegramBotApi
) : TelegramBotApiRepository {
    override suspend fun getBotDetails(botApiToken: String): Result<TelegramBot> {
        val response = telegramBotApi.getMe(botApiToken)
        return if (response.isSuccessful && response.body() != null) {
            // TODO: Why do we have body()!! here?
            Result.success(response.body()!!.result.toBotDetails())
        } else {
            if (response.code() == 401) {
                Result.failure(IllegalArgumentException("Telegram bot API token invalid"))
            } else {
                Result.failure(IOException("Telegram bot API request failed"))
            }
        }
    }

    override suspend fun getMessages(
        botApiToken: String, longPollingTimeout: Duration
    ): Result<List<TelegramPrivateChatMessage>> {
        // TODO:
        // 1. Request updates from API with polling timeout
        // 2. Limit updates to messages only
        val getUpdatesResponse = telegramBotApi.getUpdates(
            token = botApiToken,
            timeout = longPollingTimeout.inWholeMilliseconds,
            allowedUpdates = listOf("message")
        )

        return if (getUpdatesResponse.isSuccessful && getUpdatesResponse.body() != null) {
            // TODO: Why do we have body()!! here?
            Result.success(getUpdatesResponse.body()!!.result.map { it.message!!.toTelegramMessage() })
        } else {
            Result.failure<List<TelegramPrivateChatMessage>>(IOException("API request failed"))
        }
    }

    override suspend fun sendMessage(
        botApiToken: String, text: String, chatId: Long
    ): Result<Unit> {
        val sendMessageResponse =
            telegramBotApi.sendMessage(token = botApiToken, text = text, chatId = chatId)
        return if (sendMessageResponse.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(IOException("API request failed"))
        }
    }
}

private fun TelegramBotApiUserDto.toBotDetails(): TelegramBot {
    require(this.isBot) { "User is not a bot" }
    return TelegramBot(
        id = this.id, name = this.firstName, username = this.username!!
    )
}

private fun TelegramMessageDto.toTelegramMessage(): TelegramPrivateChatMessage {
    return TelegramPrivateChatMessage(
        // TODO: Verify that the chat is a private chat
        from = this.from!!.toTelegramUser(), text = this.text
    )
}

private fun TelegramBotApiUserDto.toTelegramUser(): TelegramUser {
    return TelegramUser(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
    )
}
