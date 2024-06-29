package ag.sokolov.smsrelay.ui.setup.screen.bot

import kotlinx.serialization.Serializable

@Serializable
sealed class BotSetupState {
    @Serializable
    data object NotConfigured : BotSetupState()

    @Serializable
    data object Loading : BotSetupState()

    @Serializable
    data class Configured(
        val botName: String,
        val botUsername: String
    ) : BotSetupState()

    @Serializable
    data class Error(val errorMessage: String) : BotSetupState()
}
