package ag.sokolov.smsrelay.domain.models

data class TelegramPrivateChatMessage(
    val from: TelegramUser,
    val text: String?
)
