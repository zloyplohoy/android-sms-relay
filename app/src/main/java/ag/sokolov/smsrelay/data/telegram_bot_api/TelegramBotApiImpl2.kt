package ag.sokolov.smsrelay.data.telegram_bot_api

import ag.sokolov.smsrelay.data.telegram_bot_api.retrofit.RetrofitTelegramBotApi
import ag.sokolov.smsrelay.data.telegram_bot_api.retrofit.dto.UserDto
import ag.sokolov.smsrelay.data.telegram_config.TelegramConfig
import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.model.TelegramUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.IOException
import javax.inject.Inject

internal class TelegramBotApiImpl2 @Inject constructor(
    okHttpCallFactory: dagger.Lazy<Call.Factory>,
    private val telegramConfig: TelegramConfig
) : TelegramBotApi2 {

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
            Response.Failure(
                when (e.code()) {
                    401 -> DomainError.BotApiTokenInvalid
                    else -> DomainError.UnhandledError
                }
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTelegramBot(): Flow<Response<TelegramBot, DomainError>> =
        telegramConfig.getTokenFlow().flatMapConcat { token ->
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
    override fun getTelegramRecipient(): Flow<Response<TelegramUser?, DomainError>> =
        combine(
            telegramConfig.getTokenFlow(),
            telegramConfig.getRecipientIdFlow()
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
}
