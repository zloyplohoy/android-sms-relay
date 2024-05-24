package ag.sokolov.smsrelay.data.repository

import ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.TelegramBotApiService
import ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.dto.TelegramMessageDto
import ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.dto.TelegramUserDto
import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.model.TelegramPrivateChatMessage
import ag.sokolov.smsrelay.domain.model.TelegramUser
import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository
import java.io.IOException
import javax.inject.Inject
import kotlin.time.Duration
import retrofit2.HttpException

class TelegramBotApiRepositoryImpl
@Inject
constructor(
    private val telegramBotApiService: TelegramBotApiService,
) : TelegramBotApiRepository {
    override suspend fun getTelegramBot(botApiToken: String): Response<TelegramBot, DomainError> =
        try {
            Response.Success(telegramBotApiService.getMe(botApiToken).result.toBotInfo())
        } catch (e: IOException) {
            Response.Failure(DomainError.NetworkUnavailable)
        } catch (e: HttpException) {
            Response.Failure(
                when (e.code()) {
                    // TODO: Validate token on input instead of handling 404
                    // When token is too short, 404 is returned instead of 401
                    401,
                    404 -> DomainError.BotApiTokenInvalid
                    else -> DomainError.UnhandledError
                })
        }

    override suspend fun getTelegramRecipient(
        botApiToken: String,
        recipientId: Long
    ): Response<TelegramUser, DomainError> =
        try {
            Response.Success(
                telegramBotApiService.getChat(botApiToken, recipientId).result.toTelegramUser())
        } catch (e: IOException) {
            Response.Failure(DomainError.NetworkUnavailable)
        } catch (e: HttpException) {
            Response.Failure(
                when (e.code()) {
                    400 -> DomainError.RecipientInvalid
                    401 -> DomainError.BotApiTokenInvalid
                    else -> DomainError.UnhandledError
                })
        }

    // TODO: Rewrite with handler
    override suspend fun getMessages(
        botApiToken: String,
        longPollingTimeout: Duration
    ): Result<List<TelegramPrivateChatMessage>> {
        TODO()
    }

    // TODO: Rewrite with handler
    override suspend fun sendMessage(
        botApiToken: String,
        text: String,
        chatId: Long
    ): Result<Unit> {
        TODO()
    }
}

private fun TelegramUserDto.toBotInfo(): TelegramBot {
    return TelegramBot(name = this.firstName, username = this.username!!)
}

private fun TelegramUserDto.toTelegramUser(): TelegramUser {
    return TelegramUser(
        firstName = this.firstName, lastName = this.lastName, username = this.username)
}

private fun TelegramMessageDto.toTelegramMessage(): TelegramPrivateChatMessage {
    return TelegramPrivateChatMessage(from = this.from!!.toTelegramUser(), text = this.text)
}
