package ag.sokolov.smsrelay.ui.settings.main

data class SettingsScreenState(
    val isLoading: Boolean = true,
    val botStatusDescription: String = "Loading...",
    val showBotWarning: Boolean = false,
    val recipientStatusDescription: String = "Loading...",
    val showRecipientWarning: Boolean = false,
    val permissionsConfiguration: String = "Loading..."
)
