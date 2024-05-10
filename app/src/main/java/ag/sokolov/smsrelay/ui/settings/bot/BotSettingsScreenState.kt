package ag.sokolov.smsrelay.ui.settings.bot

data class BotSettingsScreenState(
    val isBotConfigured: Boolean = true,
    val botTitle: String = "Loading...",
    val botDescription: String? = null,
    val showWarning: Boolean = false,
    val showDeleteButton: Boolean = false,
    val showTokenDialog: Boolean = false
)
