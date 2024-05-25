package ag.sokolov.smsrelay.ui.settings

import androidx.compose.runtime.Stable

@Stable
data class SettingsState(
    val isLoading: Boolean = true,
    val permissionsConfiguration: String = "Loading...",
    val botState: BotState = BotState.Loading,
    val recipientState: RecipientState = RecipientState.Loading
)
