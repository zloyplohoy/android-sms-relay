package ag.sokolov.smsrelay.ui.settings.common

import androidx.compose.runtime.Stable

@Stable
data class SettingsState(
    val isLoading: Boolean = true,
    val botState: BotState = BotState.Loading,
    val recipientState: RecipientState = RecipientState.Loading
)
