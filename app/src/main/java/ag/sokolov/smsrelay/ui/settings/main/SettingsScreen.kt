package ag.sokolov.smsrelay.ui.settings.main

import ag.sokolov.smsrelay.ui.common.MenuHeader
import ag.sokolov.smsrelay.ui.settings.common.BotState
import ag.sokolov.smsrelay.ui.settings.common.RecipientState
import ag.sokolov.smsrelay.ui.settings.common.SettingsState
import ag.sokolov.smsrelay.ui.settings.navigation.SettingsNavRoutes
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SettingsScreen(state: SettingsState = SettingsState(), navigate: (String) -> Unit = {}) {
    Column(modifier = Modifier.fillMaxWidth()) {
        MenuHeader(title = "Settings", isLoading = state.isLoading)
        TelegramBotMenuItem(
            botState = state.botState, onClick = { navigate(SettingsNavRoutes.BOT) })
        TelegramRecipientMenuItem(
            recipientState = state.recipientState,
            onClick = { navigate(SettingsNavRoutes.RECIPIENT) })
    }
}

@Preview
@Composable
private fun PreviewSettingsScreenLoading() {
    SMSRelayTheme { Surface { SettingsScreen() } }
}

@Preview
@Composable
private fun PreviewSettingsScreenConfigured() {
    SMSRelayTheme {
        Surface {
            SettingsScreen(
                state =
                    SettingsState(
                        isLoading = false,
                        botState =
                            BotState.Configured(
                                botName = "Awesome SMS bot", botUsername = "awesome_sms_bot"),
                        recipientState = RecipientState.Configured(fullName = "Aleksei Sokolov")))
        }
    }
}
