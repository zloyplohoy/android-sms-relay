package ag.sokolov.smsrelay.ui.settings.main

data class SettingsScreenState (
    val botDescription: String? = null,
    val showBotWarning: Boolean = false,
    val recipientConfiguration: String = "Loading...",
    val permissionsConfiguration: String = "Loading..."
)
