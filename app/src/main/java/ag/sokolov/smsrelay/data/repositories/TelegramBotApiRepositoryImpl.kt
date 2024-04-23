package ag.sokolov.smsrelay.data.repositories

import ag.sokolov.smsrelay.data.sources.remote.apis.telegram_bot.TelegramBotApiService
import ag.sokolov.smsrelay.data.sources.remote.apis.telegram_bot.dtos.TelegramMessageDto
import ag.sokolov.smsrelay.data.sources.remote.apis.telegram_bot.dtos.TelegramUserDto
import ag.sokolov.smsrelay.domain.errors.DomainException
import ag.sokolov.smsrelay.domain.models.TelegramBotInfo
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

    override suspend fun getBotInfo(botApiToken: String): Result<TelegramBotInfo> = runCatching {
        handleRequestExceptions { telegramBotApiService.getMe(botApiToken) }.result.toBotInfo()
    }

    private suspend fun <T> handleRequestExceptions(request: suspend () -> Response<T>): T {
        try {
            val response = request()
            throwIfUnauthorized(response)
            throwIfUnsuccessful(response)
            throwIfEmptyBody(response)
            return response.body()!!
        } catch (e: DomainException) {
            throw e
        } catch (exception: IOException) {
            throw DomainException.BotNetworkException()
        } catch (exception: Exception) {
            throw DomainException.UnhandledBotException()
        }
    }

    private fun <T> throwIfUnauthorized(response: Response<T>) {
        if (response.code() == 401) {
            throw DomainException.BotUnauthorizedException()
        }
    }

    private fun <T> throwIfUnsuccessful(response: Response<T>) {
        if (!response.isSuccessful) {
            throw DomainException.UnhandledBotException()
        }
    }

    private fun <T> throwIfEmptyBody(response: Response<T>) {
        if (response.body() == null) {
            throw DomainException.UnhandledBotException()
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

private fun TelegramUserDto.toBotInfo(): TelegramBotInfo {
    require(this.isBot) { "User is not a bot" }
    return TelegramBotInfo(
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
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
    )
}
