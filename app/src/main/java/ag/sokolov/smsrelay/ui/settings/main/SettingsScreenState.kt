package ag.sokolov.smsrelay.ui.settings.main

data class SettingsScreenState (
    val botDescription: String = "Loading...",
    val showBotWarning: Boolean = false,
    val recipientConfiguration: String = "Loading...",
    val permissionsConfiguration: String = "Loading..."
)
