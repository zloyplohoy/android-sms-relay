package ag.sokolov.smsrelay.data.telegram_bot_api

import ag.sokolov.smsrelay.data.telegram_bot_api.remote.TelegramBotApiService
import ag.sokolov.smsrelay.data.telegram_bot_api.remote.dto.TelegramMessageDto
import ag.sokolov.smsrelay.data.telegram_bot_api.remote.dto.TelegramUserDto
import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.model.TelegramPrivateChatMessage
import ag.sokolov.smsrelay.domain.model.TelegramUser
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.time.Duration

class TelegramBotApiImpl
@Inject constructor(
    private val telegramBotApiService: TelegramBotApiService,
) : TelegramBotApi {
    override suspend fun getTelegramBot(botApiToken: String): Response<TelegramBot, DomainError> =
        try {
            Response.Success(telegramBotApiService.getMe(botApiToken).result.toBotInfo())
        } catch (e: IOException) {
            Response.Failure(DomainError.NetworkError)
        } catch (e: HttpException) {
            Response.Failure(
                when (e.code()) {
                    // TODO: Validate token on input instead of handling 404
                    // When token is too short, 404 is returned instead of 401
                    401, 404 -> DomainError.BotApiTokenInvalid
                    else -> DomainError.UnhandledError
                }
            )
        }

    override suspend fun getTelegramRecipient(
        botApiToken: String,
        recipientId: Long
    ): Response<TelegramUser, DomainError> =
        try {
            Response.Success(
                telegramBotApiService.getChat(botApiToken, recipientId).result.toTelegramUser()
            )
        } catch (e: IOException) {
            Response.Failure(DomainError.NetworkError)
        } catch (e: HttpException) {
            Response.Failure(
                when (e.code()) {
                    // TODO: Validate token on input instead of handling 404
                    // When token is too short, 404 is returned instead of 401
                    400 -> DomainError.RecipientInvalid
                    401, 404 -> DomainError.BotApiTokenInvalid
                    else -> DomainError.UnhandledError
                }
            )
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

// TODO: Get rid of assertive reference
private fun TelegramUserDto.toBotInfo(): TelegramBot =
    TelegramBot(
        name = this.firstName,
        username = this.username!!
    )

private fun TelegramUserDto.toTelegramUser(): TelegramUser =
    TelegramUser(
        firstName = this.firstName,
        lastName = this.lastName,
        username = this.username
    )

// TODO: Get rid of assertive reference
private fun TelegramMessageDto.toTelegramMessage(): TelegramPrivateChatMessage =
    TelegramPrivateChatMessage(
        from = this.from!!.toTelegramUser(),
        text = this.text
    )
