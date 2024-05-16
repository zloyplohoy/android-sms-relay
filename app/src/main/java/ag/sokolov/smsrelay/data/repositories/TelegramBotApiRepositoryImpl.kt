package ag.sokolov.smsrelay.data.repositories

import ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.TelegramBotApiService
import ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.dto.TelegramMessageDto
import ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.dto.TelegramUserDto
import ag.sokolov.smsrelay.domain.errors.TelegramBotException
import ag.sokolov.smsrelay.domain.models.TelegramBot
import ag.sokolov.smsrelay.domain.models.TelegramPrivateChatMessage
import ag.sokolov.smsrelay.domain.models.TelegramUser
import ag.sokolov.smsrelay.domain.repositories.TelegramBotApiRepository
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import kotlin.time.Duration


class TelegramBotApiRepositoryImpl @Inject constructor(
    private val telegramBotApiService: TelegramBotApiService
) : TelegramBotApiRepository {

    override suspend fun getTelegramBot(botApiToken: String): Result<TelegramBot> = runCatching {
        handleRequestExceptions { telegramBotApiService.getMe(botApiToken) }.result.toBotInfo()
    }

    override suspend fun getChat(chatId: Long, botApiToken: String): Result<TelegramUser> = runCatching {
        handleRequestExceptions { telegramBotApiService.getChat(botApiToken, chatId) }.result.toTelegramUser()
    }

    private suspend fun <T> handleRequestExceptions(request: suspend () -> Response<T>): T {
        try {
            val response = request()
            throwIfUnauthorized(response)
            throwIfUnsuccessful(response)
            throwIfEmptyBody(response)
            return response.body()!!
        } catch (e: TelegramBotException) {
            throw e
        } catch (exception: IOException) {
            throw TelegramBotException.NetworkUnavailable()
        } catch (exception: Exception) {
            throw TelegramBotException.UnhandledException()
        }
    }

    private fun <T> throwIfUnauthorized(response: Response<T>) {
        if (response.code() == 401) {
            throw TelegramBotException.BotApiTokenInvalid()
        }
    }

    private fun <T> throwIfUnsuccessful(response: Response<T>) {
        if (!response.isSuccessful) {
            throw TelegramBotException.UnhandledException()
        }
    }

    private fun <T> throwIfEmptyBody(response: Response<T>) {
        if (response.body() == null) {
            throw TelegramBotException.UnhandledException()
        }
    }

    // TODO: Rewrite with handler
    override suspend fun getMessages(
        botApiToken: String, longPollingTimeout: Duration
    ): Result<List<TelegramPrivateChatMessage>> {
        // TODO:
        // 1. Request updates from API with polling timeout
        // 2. Limit updates to messages only
        val getUpdatesResponse = telegramBotApiService.getUpdates(
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

    // TODO: Rewrite with handler
    override suspend fun sendMessage(
        botApiToken: String, text: String, chatId: Long
    ): Result<Unit> {
        val sendMessageResponse =
            telegramBotApiService.sendMessage(token = botApiToken, text = text, chatId = chatId)
        return if (sendMessageResponse.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(IOException("API request failed"))
        }
    }
}

private fun TelegramUserDto.toBotInfo(): TelegramBot {
    return TelegramBot(
        name = this.firstName,
        username = this.username!!
    )
}

private fun TelegramMessageDto.toTelegramMessage(): TelegramPrivateChatMessage {
    return TelegramPrivateChatMessage(
        // TODO: Verify that the chat is a private chat
        from = this.from!!.toTelegramUser(),
        text = this.text
    )
}

private fun TelegramUserDto.toTelegramUser(): TelegramUser {
    return TelegramUser(
        firstName = this.firstName,
        lastName = this.lastName,
        username = this.username
    )
}
