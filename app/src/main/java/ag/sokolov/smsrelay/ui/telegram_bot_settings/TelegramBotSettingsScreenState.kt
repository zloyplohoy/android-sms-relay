package ag.sokolov.smsrelay.ui.telegram_bot_settings

data class TelegramBotSettingsScreenState(
    val isBotAdded: Boolean = false,
    val botUsername: String = "",
    val botName: String = "",
)
