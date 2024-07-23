package ag.sokolov.smsrelay.ui.setup.screen.bot

import kotlinx.serialization.Serializable

@Serializable
sealed class BotState {
    @Serializable
    data object NotConfigured : BotState()

    @Serializable
    data object Loading : BotState()

    @Serializable
    data class Configured(
        val botName: String,
        val botUsername: String
    ) : BotState()

    @Serializable
    data class Error(val errorMessage: String) : BotState()
}
