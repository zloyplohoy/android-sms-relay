package ag.sokolov.smsrelay.ui.settings.bot

sealed class BotSettingsAction {
    data class AddBot(val botApiToken: String) : BotSettingsAction()
    data object RemoveBot : BotSettingsAction()
}
