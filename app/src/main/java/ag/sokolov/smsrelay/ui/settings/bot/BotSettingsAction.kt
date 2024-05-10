package ag.sokolov.smsrelay.ui.settings.bot

sealed class BotSettingsAction {
    data object ToggleTokenDialog : BotSettingsAction()
    data object RemoveBot : BotSettingsAction()
    data class AddBot(val botApiToken: String) : BotSettingsAction()
}
