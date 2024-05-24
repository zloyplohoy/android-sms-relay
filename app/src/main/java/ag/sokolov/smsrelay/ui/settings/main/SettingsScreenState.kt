package ag.sokolov.smsrelay.ui.settings.main

data class SettingsScreenState(
    val botStatusDescription: String = "Loading...",
    val showBotWarning: Boolean = false,
    val allowRecipientConfiguration: Boolean = false,
    val recipientStatusDescription: String = "Loading...",
    val showRecipientWarning: Boolean = false,
    val permissionsConfiguration: String = "Loading...",
    val botSettingsState: BotSettingsState = BotSettingsState.Loading
)

sealed class BotSettingsState {
    data object Loading : BotSettingsState()

    data object NotConfigured : BotSettingsState()

    data class Configured(val name: String, val username: String) : BotSettingsState()

    data class Error(val message: String) : BotSettingsState()
}
