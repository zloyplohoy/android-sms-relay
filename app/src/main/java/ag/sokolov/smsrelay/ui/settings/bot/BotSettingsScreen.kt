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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BotSettingsScreen(
    state: BotSettingsScreenState, onAction: (BotSettingsAction) -> Unit, onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        MenuHeader(title = "Telegram bot", onBackClick = { onBackClick() })
        MenuItem(
            icon = if (!state.isBotConfigured) Icons.Filled.Add else null,
            title = state.botTitle,
            description = state.botDescription,
            onClick = if (!state.isBotConfigured || state.showWarning) ({
                onAction(
                    BotSettingsAction.ToggleTokenDialog
                )
            }) else null,
            extraIcon = if (state.showWarning) Icons.Filled.Warning else if (state.showDeleteButton) Icons.Filled.Clear else null,
            onExtraClick = if (state.isBotConfigured && !state.showWarning) ({
                onAction(BotSettingsAction.RemoveBot)
            }) else null
        )
    }
    if (state.showTokenDialog) {
        BotApiTokenDialog(
            onAction = onAction
        )
    }
}

@Preview
@Composable
private fun PreviewBotSettingsScreenLoading() {
    SMSRelayTheme {
        Surface {
            BotSettingsScreen(state = BotSettingsScreenState(), onAction = {}, onBackClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewBotSettingsScreenNotConfigured() {
    SMSRelayTheme {
        Surface {
            BotSettingsScreen(state = BotSettingsScreenState(
                isBotConfigured = false, botTitle = "Add a Telegram bot"
            ), onAction = {}, onBackClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewBotSettingsScreenConfigured() {
    SMSRelayTheme {
        Surface {
            BotSettingsScreen(state = BotSettingsScreenState(
                isBotConfigured = true,
                botTitle = "Awesome Telegram Bot",
                botDescription = "@awesome_telegram_bot",
                showDeleteButton = true
            ), onAction = {}, onBackClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewBotSettingsScreenError() {
    SMSRelayTheme {
        Surface {
            BotSettingsScreen(state = BotSettingsScreenState(
                isBotConfigured = true,
                botTitle = "Error",
                botDescription = "Bot token invalid",
                showWarning = true
            ), onAction = {}, onBackClick = {})
        }
    }
}
