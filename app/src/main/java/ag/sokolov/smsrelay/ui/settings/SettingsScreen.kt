package ag.sokolov.smsrelay.ui.settings

import ag.sokolov.smsrelay.ui.common.ScreenTitle
import ag.sokolov.smsrelay.ui.common.SettingsItem
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = hiltViewModel(),
    navigateToRoute: (String) -> Unit = {}
) {
    SettingsScreenContent(
        state = viewModel.state.value,
        navigateToRoute = navigateToRoute
    )
}

@Composable
fun SettingsScreenContent(
    state: SettingsScreenState = SettingsScreenState(),
    navigateToRoute: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ScreenTitle(title = "Settings")
        SettingsItem(
            icon = Icons.AutoMirrored.Outlined.Send,
            title = "Telegram bot",
            subtitle = state.botConfiguration,
            onClick = { navigateToRoute("telegram_bot_settings") }
        )
        SettingsItem(
            icon = Icons.Outlined.Person,
            title = "Telegram recipient",
            subtitle = "Not configured",
            onClick = { navigateToRoute("telegram_recipient_settings") }
        )
        SettingsItem(
            icon = Icons.Outlined.MailOutline,
            title = "SMS permissions",
            subtitle = "Not granted",
            onClick = { navigateToRoute("telegram_bot_settings") }
        )
    }
}

@Preview
@Composable
private fun PreviewSettingsScreen() {
    SMSRelayTheme {
        Surface {
            SettingsScreenContent()
        }
    }
}

@Preview
@Composable
private fun PreviewSettingsScreenFilled() {
    SMSRelayTheme {
        Surface {
            SettingsScreenContent(
                state = SettingsScreenState(
                    botConfiguration = "@sms_relay_bot"
                )
            )
        }
    }
}
