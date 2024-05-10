package ag.sokolov.smsrelay.domain.models

data class TelegramSettings(
    val telegramBotApiToken: String? = null,
    val recipientChatId: Long? = null
)
