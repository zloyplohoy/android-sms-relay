package ag.sokolov.smsrelay.data.repository

import ag.sokolov.smsrelay.data.repository.api.TelegramBotApi
import ag.sokolov.smsrelay.data.repository.api.dto.TelegramBotApiUserDto
import ag.sokolov.smsrelay.domain.model.BotDetails
import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository
import java.io.IOException
import javax.inject.Inject


class TelegramBotApiRepositoryImpl @Inject constructor(
    private val telegramBotApi: TelegramBotApi
) : TelegramBotApiRepository {
    override suspend fun getBotDetails(botApiToken: String): Result<BotDetails> {
        val response = telegramBotApi.getMe(botApiToken)
        return if (response.isSuccessful && response.body() != null) {
            Result.success(response.body()!!.result.toBotDetails())
        } else {
            if (response.code() == 401) {
                Result.failure(IllegalArgumentException("Telegram bot API token invalid"))
            } else {
                Result.failure(IOException("Telegram bot API request failed"))
            }
        }
    }
}

private fun TelegramBotApiUserDto.toBotDetails(): BotDetails {
    require(this.isBot) { "User is not a bot" }
    return BotDetails(
        id = this.id, name = this.firstName, username = this.username!!
    )
}

