package ag.sokolov.smsrelay.domain.models

data class TelegramUser (
    val firstName: String,
    val lastName: String? = null,
    val username: String? = null
)
