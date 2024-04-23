package ag.sokolov.smsrelay.ui.telegram_bot_settings

data class TelegramBotSettingsScreenState(
    val isBotRegistered: Boolean = false,
    val botUsername: String = "",
    val botName: String = "",
)
