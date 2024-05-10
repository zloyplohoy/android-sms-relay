package ag.sokolov.smsrelay.ui.settings.main

import ag.sokolov.smsrelay.ui.common.MenuHeader
import ag.sokolov.smsrelay.ui.common.MenuItem
import ag.sokolov.smsrelay.ui.settings.navigation.SettingsNavRoutes
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

@Composable
fun SettingsScreen(
    state: SettingsScreenState = SettingsScreenState(), navigate: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        MenuHeader(title = "Settings")
        MenuItem(
            icon = Icons.AutoMirrored.Outlined.Send,
            title = "Telegram bot",
            description = state.botDescription,
            onClick = { navigate(SettingsNavRoutes.BOT) })
        MenuItem(
            icon = Icons.Outlined.Person,
            title = "Recipient",
            description = state.recipientConfiguration,
            onClick = { navigate(SettingsNavRoutes.RECIPIENT) })
        MenuItem(
            icon = Icons.AutoMirrored.Outlined.List,
            title = "Permissions",
            description = state.permissionsConfiguration,
            onClick = { navigate(SettingsNavRoutes.PERMISSIONS) })
    }
}

@Preview
@Composable
private fun PreviewSettingsScreen() {
    SMSRelayTheme {
        Surface {
            SettingsScreen()
        }
    }
}

@Preview
@Composable
private fun PreviewSettingsScreenFilled() {
    SMSRelayTheme {
        Surface {
            SettingsScreen(
                state = SettingsScreenState(
                    botDescription = "@sms_relay_bot"
                )
            )
        }
    }
}
