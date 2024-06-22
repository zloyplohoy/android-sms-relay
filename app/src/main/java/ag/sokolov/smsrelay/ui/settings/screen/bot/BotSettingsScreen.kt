package ag.sokolov.smsrelay.ui.settings.screen.bot

import ag.sokolov.smsrelay.ui.common.element.menu_item.MenuItem
import ag.sokolov.smsrelay.ui.common.element.screen_top_bar.ScreenTopBar
import ag.sokolov.smsrelay.ui.settings.action.SettingsAction
import ag.sokolov.smsrelay.ui.settings.state.BotState
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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

    Column {
        ScreenTopBar(title = "Telegram bot", onBackClick = { onBackClick() })
        when (state) {
            is BotState.Loading -> MenuItem(title = "Loading")
            is BotState.NotConfigured -> MenuItem(
                icon = Icons.Filled.Add,
                title = "Add a bot",
                onClick = { toggleTokenDialog() })
            is BotState.Configured -> MenuItem(
                title = state.botName, description = "@${state.botUsername}"
            )
            is BotState.Error -> MenuItem(title = "Error", description = state.errorMessage)
        }
    }

    if (showTokenDialog) BotApiTokenDialog(toggleDialog = ::toggleTokenDialog, onAction = onAction)
}

@Preview
@Composable
private fun PreviewBotSettingsScreenLoading() {
    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            BotSettingsScreen(state = BotState.Loading, onAction = {}, onBackClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewBotSettingsScreenNotConfigured() {
    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            BotSettingsScreen(state = BotState.NotConfigured, onAction = {}, onBackClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewBotSettingsScreenConfigured() {
    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            BotSettingsScreen(state = BotState.Configured(
                botName = "My awesome bot", botUsername = "my_awesome_bot"
            ), onAction = {}, onBackClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewBotSettingsScreenError() {
    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            BotSettingsScreen(state = BotState.Error("API token invalid"),
                onAction = {},
                onBackClick = {})
        }
    }
}
