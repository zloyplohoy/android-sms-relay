package ag.sokolov.smsrelay.ui.settings

import ag.sokolov.smsrelay.ui.common.ScreenTitle
import ag.sokolov.smsrelay.ui.common.SettingsItem
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

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
            description = state.botConfiguration,
            isClickable = true,
            onClick = { navigateToRoute("telegram_bot_settings") }
        )
        SettingsItem(
            icon = Icons.Outlined.Person,
            title = "Recipient",
            description = "Not configured",
            isClickable = true,
            onClick = { navigateToRoute("telegram_recipient_settings") }
        )
        SettingsItem(
            icon = Icons.AutoMirrored.Outlined.List,
            title = "Permissions",
            description = "Not granted",
            isClickable = true,
            onClick = { navigateToRoute("system_permissions_settings") }
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
