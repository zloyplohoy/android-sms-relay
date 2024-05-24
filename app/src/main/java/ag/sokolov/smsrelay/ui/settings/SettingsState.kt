package ag.sokolov.smsrelay.ui.settings

import androidx.compose.runtime.Stable

@Stable
data class SettingsState(
    val isLoading: Boolean = true,
    val recipientStatusDescription: String = "Loading...",
    val showRecipientWarning: Boolean = false,
    val permissionsConfiguration: String = "Loading...",
    val botState: BotState = BotState.Loading
)
