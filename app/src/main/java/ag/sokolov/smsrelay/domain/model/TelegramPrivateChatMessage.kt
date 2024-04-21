package ag.sokolov.smsrelay.domain.model

data class TelegramPrivateChatMessage(
    val from: TelegramUser,
    val text: String?
)
