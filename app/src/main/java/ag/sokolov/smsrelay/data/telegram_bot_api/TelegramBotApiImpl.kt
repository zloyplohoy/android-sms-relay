package ag.sokolov.smsrelay.data.telegram_bot_api

import ag.sokolov.smsrelay.core.data.repository.TelegramConfigRepository
import ag.sokolov.smsrelay.data.constants.Constants.TELEGRAM_BOT_API_LONG_POLLING_TIMEOUT
import ag.sokolov.smsrelay.data.telegram_bot_api.retrofit.RetrofitTelegramBotApi
import ag.sokolov.smsrelay.data.telegram_bot_api.retrofit.dto.MessageDto
import ag.sokolov.smsrelay.data.telegram_bot_api.retrofit.dto.UserDto
import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.model.TelegramPrivateChatMessage
import ag.sokolov.smsrelay.domain.model.TelegramUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.IOException
import javax.inject.Inject

internal class TelegramBotApiImpl @Inject constructor(
    okHttpCallFactory: dagger.Lazy<Call.Factory>,
    private val telegramConfigRepository: TelegramConfigRepository
) : TelegramBotApi {

    private val json = Json { ignoreUnknownKeys = true }
    private val jsonMediaType = "application/json".toMediaType()

    private val telegramBotApiService: RetrofitTelegramBotApi =
        Retrofit.Builder()
            .baseUrl("https://api.telegram.org/")
            .callFactory { okHttpCallFactory.get().newCall(it) }
            .addConverterFactory(json.asConverterFactory(jsonMediaType))
            .build()
            .create(RetrofitTelegramBotApi::class.java)

    private suspend fun <T> getApiResponse(request: suspend () -> T) =
        try {
            Response.Success(request())
        } catch (e: IOException) {
            Response.Failure(DomainError.NetworkError)
        } catch (e: HttpException) {
            Response.Failure(mapHttpExceptionToDomainError(e))
        }

    private fun mapHttpExceptionToDomainError(e: HttpException): DomainError =
        when (e.code()) {
            401 -> DomainError.BotApiTokenInvalid
            else -> DomainError.UnhandledError
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTelegramBotFlow(): Flow<Response<TelegramBot?, DomainError>> =
        telegramConfigRepository.getTokenFlow().flatMapConcat { token ->
            token?.let {
                flow {
                    emit(Response.Loading)
                    emit(getApiResponse { telegramBotApiService.getMe(token).result.toBotInfo() })
                }
            } ?: flow { emit(Response.Success()) }
        }

    private fun UserDto.toBotInfo(): TelegramBot =
        TelegramBot(
            name = this.firstName,
            username = this.username!!
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTelegramRecipientFlow(): Flow<Response<TelegramUser?, DomainError>> =
        combine(
            telegramConfigRepository.getTokenFlow(),
            telegramConfigRepository.getRecipientIdFlow()
        ) { token, recipientId ->
            token to recipientId
        }.flatMapConcat { (token, recipientId) ->
            if (token != null && recipientId != null) {
                flow {
                    emit(Response.Loading)
                    emit(getApiResponse {
                        telegramBotApiService.getChat(token, recipientId).result.toTelegramUser()
                    })
                }
            } else {
                flow { emit(Response.Success()) }
            }
        }

    private fun UserDto.toTelegramUser(): TelegramUser =
        TelegramUser(
            id = this.id,
            firstName = this.firstName,
            lastName = this.lastName,
            username = this.username
        )

    override fun getMessagesFlow(): Flow<TelegramPrivateChatMessage> =
        flow {
            telegramConfigRepository.getToken()?.let { token ->
                val timeout = TELEGRAM_BOT_API_LONG_POLLING_TIMEOUT.inWholeSeconds
                var offset: Long = 0
                val allowedUpdates = listOf("message")

                while (true) {
                    telegramBotApiService.getUpdates(token, timeout, offset, allowedUpdates).result
                        .filter { it.message != null }
                        .also {
                            if (it.isNotEmpty()) {
                                offset = it.last().updateId + 1
                            }
                        }
                        .onEach { emit(it.message!!.toTelegramMessage()) }
                }
            }
        }.retryWhen { cause, attempt -> cause is IOException && attempt < 3 }.catch { }

    private fun MessageDto.toTelegramMessage(): TelegramPrivateChatMessage =
        TelegramPrivateChatMessage(
            from = this.from!!.toTelegramUser(),
            text = this.text
        )

    override suspend fun sendMessage(message: String) {
        val token = telegramConfigRepository.getToken()
        val recipientId = telegramConfigRepository.getRecipientId()

        if (token != null && recipientId != null) {
            try {
                telegramBotApiService.sendMessage(token, recipientId, message)
            } catch (_: Exception) {
                // TODO: Verify successful delivery
            }
        }
    }
}
