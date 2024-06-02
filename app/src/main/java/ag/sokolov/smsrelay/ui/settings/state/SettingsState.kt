package ag.sokolov.smsrelay.ui.settings.state

import androidx.compose.runtime.Stable

@Stable
data class SettingsState(
    val botState: BotState = BotState.Loading,
    val recipientState: RecipientState = RecipientState.Loading()
)
