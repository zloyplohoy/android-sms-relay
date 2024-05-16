package ag.sokolov.smsrelay.ui.settings.bot

sealed class BotSettingsScreenState {
    data object Loading : BotSettingsScreenState()
    data object NotConfigured : BotSettingsScreenState()
    data class Configured(val botName: String, val botUsername: String) : BotSettingsScreenState()
    data class Error(val errorMessage: String? = null) : BotSettingsScreenState()
}
