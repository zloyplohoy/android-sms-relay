package ag.sokolov.smsrelay.ui.telegram_bot_settings

sealed class TelegramBotSettingsScreenState(
    open val isBotAdded: Boolean,
    open val botTitle: String,
    open val botDescription: String
) {
    data object Loading : TelegramBotSettingsScreenState(
        isBotAdded = false,
        botTitle = "Loading...",
        botDescription = "Requesting bot configuration"
    )

    data object NotConfigured : TelegramBotSettingsScreenState(
        isBotAdded = false,
        botTitle = "Bot not configured",
        botDescription = "Click here to set up with API token"
    )

    data class Configured(
        override val isBotAdded: Boolean,
        override val botTitle: String,
        override val botDescription: String
    ) : TelegramBotSettingsScreenState(isBotAdded, botTitle, botDescription)
}
