package ag.sokolov.smsrelay.ui.settings.bot

import ag.sokolov.smsrelay.ui.common.MenuHeader
import ag.sokolov.smsrelay.ui.common.MenuItem
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun BotSettingsScreen(
    state: BotSettingsScreenState, onAction: (BotSettingsAction) -> Unit, onBackClick: () -> Unit
) {
    var showTokenDialog by rememberSaveable { mutableStateOf(false) }

    fun toggleTokenDialog() {
        showTokenDialog = !showTokenDialog
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        MenuHeader(title = "Telegram bot", onBackClick = { onBackClick() })
        MenuItem(
            icon = if (state is BotSettingsScreenState.NotConfigured) Icons.Filled.Add else null,
            title = when (state) {
                is BotSettingsScreenState.Loading -> "Loading..."
                is BotSettingsScreenState.NotConfigured -> "Add a Telegram bot"
                is BotSettingsScreenState.Configured -> state.botName
                is BotSettingsScreenState.Error -> "Error"
            },
            description = when (state) {
                is BotSettingsScreenState.Configured -> "@${state.botUsername}"
                is BotSettingsScreenState.Error -> state.errorMessage ?: "Unhandled error"
                else -> null
            },
            onClick = when (state) {
                is BotSettingsScreenState.NotConfigured, is BotSettingsScreenState.Error -> ({ toggleTokenDialog() })
                else -> null
            },
            extraIcon = when (state) {
                is BotSettingsScreenState.Error -> Icons.Filled.Warning
                is BotSettingsScreenState.Configured -> Icons.Filled.Clear
                else -> null
            },
            onExtraClick = if (state is BotSettingsScreenState.Configured) ({
                onAction(BotSettingsAction.RemoveBot)
            }) else null
        )
    }
    if (showTokenDialog) {
        BotApiTokenDialog(
            toggleDialog = ::toggleTokenDialog, onAction = onAction
        )
    }
}

// TODO: Restore previews

//@Preview
//@Composable
//private fun PreviewBotSettingsScreenLoading() {
//    SMSRelayTheme {
//        Surface {
//            BotSettingsScreen(state = BotSettingsScreenState(), onAction = {}, onBackClick = {})
//        }
//    }
//}
//
//@Preview
//@Composable
//private fun PreviewBotSettingsScreenNotConfigured() {
//    SMSRelayTheme {
//        Surface {
//            BotSettingsScreen(state = BotSettingsScreenState(
//                isBotConfigured = false, botTitle = "Add a Telegram bot"
//            ), onAction = {}, onBackClick = {})
//        }
//    }
//}
//
//@Preview
//@Composable
//private fun PreviewBotSettingsScreenConfigured() {
//    SMSRelayTheme {
//        Surface {
//            BotSettingsScreen(state = BotSettingsScreenState(
//                isBotConfigured = true,
//                botTitle = "Awesome Telegram Bot",
//                botDescription = "@awesome_telegram_bot",
//                showDeleteButton = true
//            ), onAction = {}, onBackClick = {})
//        }
//    }
//}
//
//@Preview
//@Composable
//private fun PreviewBotSettingsScreenError() {
//    SMSRelayTheme {
//        Surface {
//            BotSettingsScreen(state = BotSettingsScreenState(
//                isBotConfigured = true,
//                botTitle = "Error",
//                botDescription = "Bot token invalid",
//                showWarning = true
//            ), onAction = {}, onBackClick = {})
//        }
//    }
//}
