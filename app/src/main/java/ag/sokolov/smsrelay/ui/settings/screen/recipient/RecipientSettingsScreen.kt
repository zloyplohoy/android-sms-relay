package ag.sokolov.smsrelay.ui.settings.screen.recipient

import ag.sokolov.smsrelay.ui.common.MenuHeader
import ag.sokolov.smsrelay.ui.common.MenuItem
import ag.sokolov.smsrelay.ui.settings.action.SettingsAction
import ag.sokolov.smsrelay.ui.settings.state.RecipientState
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RecipientSettingsScreen(
    state: RecipientState = RecipientState.Loading(),
    onAction: (SettingsAction) -> Unit,
    onBackClick: () -> Unit
) {
    Column {
        MenuHeader(title = "Recipient", onBackClick = { onBackClick() })
        when (state) {
            is RecipientState.Loading -> MenuItem(title = state.message)
            is RecipientState.NotConfigured ->
                MenuItem(
                    icon = Icons.Outlined.Add,
                    title = "Add recipient",
                    onClick = { onAction(SettingsAction.AddRecipient) })
            is RecipientState.Configured ->
                MenuItem(title = state.fullName, description = state.username?.let { "@$it" })
            is RecipientState.RecipientError ->
                MenuItem(title = "Error", description = state.errorMessage)
            is RecipientState.ExternalError ->
                MenuItem(title = state.errorMessage, showWarning = true)
        }
    }
}

@Preview
@Composable
private fun PreviewRecipientSettingsScreen() {
    SMSRelayTheme {
        Surface {
            RecipientSettingsScreen(
                state = RecipientState.Loading(), onAction = {}, onBackClick = {})
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
                state = RecipientState.ExternalError("Check bot settings"),
                onAction = {},
                onBackClick = {})
        }
    }
}
