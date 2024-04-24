package ag.sokolov.smsrelay.ui.telegram_bot_settings

sealed class TelegramBotSettingsScreenState {

    abstract val isBotAdded: Boolean
    abstract val botTitle: String
    abstract val botDescription: String

    data object Loading : TelegramBotSettingsScreenState() {
        override val isBotAdded = false
        override val botTitle = "Loading..."
        override val botDescription = "Requesting bot configuration"
    }

    data object NotConfigured : TelegramBotSettingsScreenState() {
        override val isBotAdded = false
        override val botTitle = "Bot not configured"
        override val botDescription = "Click here to set up with API token"
    }

    data class Configured(
        override val isBotAdded: Boolean,
        override val botTitle: String,
        override val botDescription: String
    ) : TelegramBotSettingsScreenState()
}
