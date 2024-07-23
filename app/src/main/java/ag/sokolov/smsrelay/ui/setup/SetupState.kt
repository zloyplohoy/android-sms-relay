package ag.sokolov.smsrelay.ui.setup

data class SetupState(
    val isLoading: Boolean = false,
    val botState: BotState = BotState.Loading
)

sealed class BotState {
    data class Configured(
        val name: String,
        val username: String
    ) : BotState()

    data class Error(
        val message: String
    ) : BotState()

    data object NotConfigured : BotState()
    data object Loading : BotState()
}
