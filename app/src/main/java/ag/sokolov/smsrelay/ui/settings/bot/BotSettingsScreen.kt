package ag.sokolov.smsrelay.ui.settings.bot

import ag.sokolov.smsrelay.ui.common.MenuHeader
import ag.sokolov.smsrelay.ui.common.MenuItem2
import ag.sokolov.smsrelay.ui.common.MenuItemClearBlock
import ag.sokolov.smsrelay.ui.settings.common.BotState
import ag.sokolov.smsrelay.ui.settings.common.SettingsAction
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
    state: BotState,
    onAction: (SettingsAction) -> Unit,
    onBackClick: () -> Unit
) {
    // TODO: If the token is failing, do not display a warning, but a clear (delete) block
    var showTokenDialog by rememberSaveable { mutableStateOf(false) }

    fun toggleTokenDialog() {
        showTokenDialog = !showTokenDialog
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        MenuHeader(
            title = "Telegram bot",
            isLoading = state is BotState.Loading,
            onBackClick = { onBackClick() })
        when (state) {
            is BotState.Loading -> Unit
            is BotState.NotConfigured ->
                MenuItem2(
                    icon = Icons.Filled.Add, title = "Add a bot", onClick = { toggleTokenDialog() })
            is BotState.Configured ->
                MenuItem2(title = state.botName, description = "@${state.botUsername}") {
                    MenuItemClearBlock(onClick = { onAction(SettingsAction.RemoveTelegramBot) })
                }
            is BotState.Error ->
                MenuItem2(title = "Error", description = state.errorMessage ?: "Unhandled error") {
                    MenuItemClearBlock(onClick = { onAction(SettingsAction.RemoveTelegramBot) })
                }
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
        Surface { BotSettingsScreen(state = BotState.Loading, onAction = {}, onBackClick = {}) }
    }
}

@Preview
@Composable
private fun PreviewBotSettingsScreenNotConfigured() {
    SMSRelayTheme {
        Surface {
            BotSettingsScreen(state = BotState.NotConfigured, onAction = {}, onBackClick = {})
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
                    BotState.Configured(botName = "My awesome bot", botUsername = "my_awesome_bot"),
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
                state = BotState.Error("Well, that escalated quickly"),
                onAction = {},
                onBackClick = {})
        }
    }
}
