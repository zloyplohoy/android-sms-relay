package ag.sokolov.smsrelay.ui.settings

sealed class SettingsAction {
    data class AddTelegramBot(val botApiToken: String) : SettingsAction()

    data object RemoveTelegramBot : SettingsAction()

    data object AddRecipient : SettingsAction()

    data object RemoveRecipient : SettingsAction()
}
