package ag.sokolov.smsrelay.ui.settings.screen.main

import ag.sokolov.smsrelay.ui.common.ScreenTopBar
import ag.sokolov.smsrelay.ui.settings.SettingsNavRoutes
import ag.sokolov.smsrelay.ui.settings.state.BotState
import ag.sokolov.smsrelay.ui.settings.state.RecipientState
import ag.sokolov.smsrelay.ui.settings.state.SettingsState
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SettingsScreen(state: SettingsState = SettingsState(), navigate: (String) -> Unit = {}) =
    Column(modifier = Modifier.fillMaxWidth()) {
        ScreenTopBar(title = "Settings")
        TelegramBotMenuItem(
            botState = state.botState,
            onClick =
                if (state.botState !is BotState.Loading) ({ navigate(SettingsNavRoutes.BOT) })
                else null)
        TelegramRecipientMenuItem(
            recipientState = state.recipientState,
            onClick =
                if (state.recipientState !is RecipientState.Loading)
                    ({ navigate(SettingsNavRoutes.RECIPIENT) })
                else null)
    }

@Preview
@Composable
private fun PreviewSettingsScreenLoading() {
    SMSRelayTheme { Surface(modifier = Modifier.fillMaxSize()) { SettingsScreen() } }
}

@Preview
@Composable
private fun PreviewSettingsScreenConfigured() {
    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SettingsScreen(
                state =
                    SettingsState(
                        botState =
                            BotState.Configured(
                                botName = "Awesome SMS bot", botUsername = "awesome_sms_bot"),
                        recipientState = RecipientState.Configured(fullName = "Aleksei Sokolov")))
        }
    }
}
