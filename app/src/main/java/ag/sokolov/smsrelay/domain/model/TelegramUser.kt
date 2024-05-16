package ag.sokolov.smsrelay.domain.model

data class TelegramUser (
    val firstName: String,
    val lastName: String? = null,
    val username: String? = null
)
