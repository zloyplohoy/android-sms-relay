package ag.sokolov.smsrelay.ui.settings

import androidx.compose.runtime.Stable

@Stable
sealed class BotState {
    data object Loading : BotState()

    data object NotConfigured : BotState()

    data class Configured(val botName: String, val botUsername: String) : BotState()

    data class Error(val errorMessage: String? = null) : BotState()
}
