package ag.sokolov.smsrelay.domain.models

data class TelegramSettingsStatus(
    val botName: String? = null,
    val botUsername: String? = null,
    val recipientName: String? = null,
    val recipientUsername: String? = null
)
