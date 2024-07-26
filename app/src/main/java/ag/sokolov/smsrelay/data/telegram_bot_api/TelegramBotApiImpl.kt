package ag.sokolov.smsrelay.data.telegram_bot_api

import ag.sokolov.smsrelay.data.constants.Constants.TELEGRAM_BOT_API_LONG_POLLING_TIMEOUT
import ag.sokolov.smsrelay.data.telegram_bot_api.retrofit.RetrofitTelegramBotApi
import ag.sokolov.smsrelay.data.telegram_bot_api.retrofit.dto.MessageDto
import ag.sokolov.smsrelay.data.telegram_bot_api.retrofit.dto.UserDto
import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.model.TelegramPrivateChatMessage
import ag.sokolov.smsrelay.domain.model.TelegramUser
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.IOException
import javax.inject.Inject

internal class TelegramBotApiImpl @Inject constructor() : TelegramBotApi {

    val json = Json { ignoreUnknownKeys = true }
    val jsonMediaType = "application/json".toMediaType()

    val telegramBotApiService =
        Retrofit.Builder()
            .baseUrl("https://api.telegram.org/")
            .addConverterFactory(json.asConverterFactory(jsonMediaType))
            .build()
            .create(RetrofitTelegramBotApi::class.java)

    // TODO: This implementation assumes that there is only one update consumer
    private var offset: Long? = null

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

    override suspend fun getMessages(
        botApiToken: String
    ): Response<List<TelegramPrivateChatMessage>, DomainError> =
        try {
            Response.Success(
                telegramBotApiService.getUpdates(
                    botApiToken,
                    TELEGRAM_BOT_API_LONG_POLLING_TIMEOUT.inWholeSeconds,
                    offset = offset,
                    allowedUpdates = listOf("message")
                ).result
                    .onEach { offset = it.updateId + 1 }
                    .mapNotNull { it.message }
                    .map { it.toTelegramMessage() }
            )
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
private fun UserDto.toBotInfo(): TelegramBot =
    TelegramBot(
        name = this.firstName,
        username = this.username!!
    )

private fun UserDto.toTelegramUser(): TelegramUser =
    TelegramUser(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        username = this.username
    )

// TODO: Get rid of assertive reference
private fun MessageDto.toTelegramMessage(): TelegramPrivateChatMessage =
    TelegramPrivateChatMessage(
        from = this.from!!.toTelegramUser(),
        text = this.text
    )
