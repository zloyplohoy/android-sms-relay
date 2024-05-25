package ag.sokolov.smsrelay.ui.settings.recipient

import ag.sokolov.smsrelay.ui.common.MenuHeader
import ag.sokolov.smsrelay.ui.common.MenuItem2
import ag.sokolov.smsrelay.ui.common.MenuItemClearBlock
import ag.sokolov.smsrelay.ui.settings.common.RecipientState
import ag.sokolov.smsrelay.ui.settings.common.SettingsAction
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RecipientSettingsScreen(
    state: RecipientState = RecipientState.Loading,
    onAction: (SettingsAction) -> Unit,
    onBackClick: () -> Unit
) {
    Column {
        MenuHeader(
            title = "Recipient",
            isLoading = state is RecipientState.Loading,
            onBackClick = { onBackClick() })
        when (state) {
            is RecipientState.Loading -> Unit
            is RecipientState.NotConfigured ->
                MenuItem2(
                    icon = Icons.Outlined.Add,
                    title = "Add recipient",
                    onClick = { onAction(SettingsAction.AddRecipient) })
            is RecipientState.Configured ->
                MenuItem2(title = state.fullName, description = state.username?.let { "@$it" }) {
                    MenuItemClearBlock(onClick = { onAction(SettingsAction.RemoveRecipient) })
                }
            is RecipientState.RecipientError ->
                MenuItem2(title = "Error", description = "Recipient blocked the bot") {
                    MenuItemClearBlock(onClick = { onAction(SettingsAction.RemoveRecipient) })
                }
            is RecipientState.BotError ->
                MenuItem2(title = "Error", description = "Check bot settings")
        }
    }
}

@Preview
@Composable
private fun PreviewRecipientSettingsScreen() {
    SMSRelayTheme {
        Surface {
            RecipientSettingsScreen(state = RecipientState.Loading, onAction = {}, onBackClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewRecipientSettingsScreenNotConfigured() {
    SMSRelayTheme {
        Surface {
            RecipientSettingsScreen(
                state = RecipientState.NotConfigured, onAction = {}, onBackClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewRecipientSettingsScreenConfigured() {
    SMSRelayTheme {
        Surface {
            RecipientSettingsScreen(
                state = RecipientState.Configured(fullName = "Aleksei Sokolov"),
                onAction = {},
                onBackClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewRecipientSettingsScreenConfiguredWithUsername() {
    SMSRelayTheme {
        Surface {
            RecipientSettingsScreen(
                state =
                    RecipientState.Configured(
                        fullName = "Aleksei Sokolov", username = "sokolov_ag"),
                onAction = {},
                onBackClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewRecipientSettingsScreenRecipientError() {
    SMSRelayTheme {
        Surface {
            RecipientSettingsScreen(
                state = RecipientState.RecipientError("Recipient blocked the bot"),
                onAction = {},
                onBackClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewRecipientSettingsScreenBotError() {
    SMSRelayTheme {
        Surface {
            RecipientSettingsScreen(
                state = RecipientState.BotError("Check bot settings"),
                onAction = {},
                onBackClick = {})
        }
    }
}
