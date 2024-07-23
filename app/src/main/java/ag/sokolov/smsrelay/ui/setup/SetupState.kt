package ag.sokolov.smsrelay.ui.setup

import kotlinx.serialization.Serializable

data class SetupState(
    var botState: BotState = BotState.Loading
)

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
