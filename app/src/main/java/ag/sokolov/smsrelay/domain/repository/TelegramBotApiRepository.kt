package ag.sokolov.smsrelay.domain.repository

import ag.sokolov.smsrelay.domain.model.BotDetails

interface TelegramBotApiRepository {
    suspend fun getBotDetails(botApiToken: String): Result<BotDetails>
}