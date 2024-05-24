package ag.sokolov.smsrelay.ui.settings.main

import ag.sokolov.smsrelay.ui.common.MenuHeader
import ag.sokolov.smsrelay.ui.common.MenuItem
import ag.sokolov.smsrelay.ui.settings.BotState
import ag.sokolov.smsrelay.ui.settings.SettingsState
import ag.sokolov.smsrelay.ui.settings.navigation.SettingsNavRoutes
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SettingsScreen(state: SettingsState = SettingsState(), navigate: (String) -> Unit = {}) {
    Column(modifier = Modifier.fillMaxWidth()) {
        MenuHeader(title = "Settings", isLoading = state.isLoading)
        TelegramBotMenuItemWrapper(
            botState = state.botState, onClick = { navigate(SettingsNavRoutes.BOT) })
        MenuItem(
            icon = Icons.Outlined.Person,
            title = "Recipient",
            description = state.recipientStatusDescription,
            onClick = { navigate(SettingsNavRoutes.RECIPIENT) },
            extraIcon = if (state.showRecipientWarning) Icons.Filled.Warning else null)
        MenuItem(
            icon = Icons.AutoMirrored.Outlined.List,
            title = "Permissions",
            description = state.permissionsConfiguration,
            onClick = { navigate(SettingsNavRoutes.PERMISSIONS) })
    }
}

@Composable
fun TelegramBotMenuItemWrapper(botState: BotState, onClick: () -> Unit = {}) {
    when (botState) {
        is BotState.Loading -> TelegramBotMenuItem(onClick = onClick, description = "Loading...")
        is BotState.Configured ->
            TelegramBotMenuItem(onClick = onClick, description = "@${botState.botUsername}")
        is BotState.NotConfigured ->
            TelegramBotMenuItem(onClick = onClick, description = "Not configured")
        is BotState.Error -> TelegramBotMenuItem(description = botState.errorMessage)
    }
}

@Composable
fun TelegramBotMenuItem(
    onClick: () -> Unit = {},
    description: String?,
    isWarningDisplayed: Boolean = false
) {
    MenuItem(
        icon = Icons.AutoMirrored.Outlined.Send,
        title = "Telegram bot",
        description = description,
        onClick = onClick,
        extraIcon = if (isWarningDisplayed) Icons.Filled.Warning else null)
}

@Preview
@Composable
private fun PreviewSettingsScreenLoading() {
    SMSRelayTheme { Surface { SettingsScreen() } }
}

@Preview
@Composable
private fun PreviewSettingsScreenFilled() {
    SMSRelayTheme {
        Surface {
            SettingsScreen(
                state =
                    SettingsState(
                        isLoading = false,
                        botState =
                            BotState.Configured(botName = "", botUsername = "awesome_sms_bot"),
                        recipientStatusDescription = "Aleksei Sokolov"))
        }
    }
}
