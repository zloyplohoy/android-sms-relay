package ag.sokolov.smsrelay.ui.settings.state

import androidx.compose.runtime.Stable

@Stable
sealed class BotState {
    data class Loading(val message: String = "Loading...") : BotState()

    data object NotConfigured : BotState()

    data class Configured(val botName: String, val botUsername: String) : BotState()

    data class Error(val errorMessage: String) : BotState()
}
