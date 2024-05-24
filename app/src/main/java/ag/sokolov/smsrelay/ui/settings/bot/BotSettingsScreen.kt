package ag.sokolov.smsrelay.ui.settings.bot

import ag.sokolov.smsrelay.ui.common.MenuHeader
import ag.sokolov.smsrelay.ui.common.MenuItem
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BotSettingsScreen(
    state: BotSettingsScreenState,
    onAction: (BotSettingsAction) -> Unit,
    onBackClick: () -> Unit
) {
    var showTokenDialog by rememberSaveable { mutableStateOf(false) }

    fun toggleTokenDialog() {
        showTokenDialog = !showTokenDialog
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        MenuHeader(title = "Telegram bot", onBackClick = { onBackClick() })
        when (state) {
            is BotSettingsScreenState.NotConfigured ->
                MenuItem(
                    icon = Icons.Filled.Add, title = "Add a bot", onClick = { toggleTokenDialog() })
            is BotSettingsScreenState.Configured ->
                MenuItem(
                    title = state.botName,
                    description = "@${state.botUsername}",
                    extraIcon = Icons.Filled.Clear,
                    onExtraClick = { onAction(BotSettingsAction.RemoveBot) })
            is BotSettingsScreenState.Error ->
                MenuItem(
                    title = "Error",
                    description = state.errorMessage ?: "Unhandled error",
                    extraIcon = Icons.Filled.Warning)
            else -> Unit
        }
    }
    if (showTokenDialog) {
        BotApiTokenDialog(toggleDialog = ::toggleTokenDialog, onAction = onAction)
    }
}

@Preview
@Composable
private fun PreviewBotSettingsScreenLoading() {
    SMSRelayTheme {
        Surface {
            BotSettingsScreen(
                state = BotSettingsScreenState.Loading, onAction = {}, onBackClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewBotSettingsScreenNotConfigured() {
    SMSRelayTheme {
        Surface {
            BotSettingsScreen(
                state = BotSettingsScreenState.NotConfigured, onAction = {}, onBackClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewBotSettingsScreenConfigured() {
    SMSRelayTheme {
        Surface {
            BotSettingsScreen(
                state =
                    BotSettingsScreenState.Configured(
                        botName = "My awesome bot", botUsername = "my_awesome_bot"),
                onAction = {},
                onBackClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewBotSettingsScreenError() {
    SMSRelayTheme {
        Surface {
            BotSettingsScreen(
                state = BotSettingsScreenState.Error("Well, that escalated quickly"),
                onAction = {},
                onBackClick = {})
        }
    }
}