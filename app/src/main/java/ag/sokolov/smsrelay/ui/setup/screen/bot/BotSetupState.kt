package ag.sokolov.smsrelay.ui.setup.screen.bot

sealed class BotSetupState {
    data object NotConfigured : BotSetupState()

    data object Loading : BotSetupState()

    data class Configured(
        val botName: String,
        val botUsername: String
    ) : BotSetupState()

    data class Error(val errorMessage: String) : BotSetupState()
}
