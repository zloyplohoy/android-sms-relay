package ag.sokolov.smsrelay.ui.settings.recipient

import ag.sokolov.smsrelay.ui.common.MenuHeader
import ag.sokolov.smsrelay.ui.common.MenuItem
import ag.sokolov.smsrelay.ui.settings.bot.BotSettingsAction
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RecipientSettingsScreen(
    state: RecipientSettingsScreenState,
    onAction: (RecipientSettingsAction) -> Unit,
    onBackClick: () -> Unit
) {
    Column {
        MenuHeader(title = "Recipient", onBackClick = { onBackClick() })
        MenuItem(
            icon = if (!state.isRecipientConfigured) Icons.Filled.Add else null,
            title = state.recipientName,
            description = state.recipientUsername,
            onClick = if (!state.isRecipientConfigured || state.showWarning) ({
                onAction(
                    RecipientSettingsAction.ToggleRecipientDialog
                )
            }) else null,
            extraIcon = if (state.showWarning) Icons.Filled.Warning else if (state.showDeleteButton) Icons.Filled.Clear else null,
            onExtraClick = if (state.isRecipientConfigured && !state.showWarning) ({
                onAction(RecipientSettingsAction.RemoveRecipient)
            }) else null
        )
//        if (state.isRecipientConfigured) {
//            SettingsItem(
//                icon = Icons.Outlined.Person,
//                title = state.recipientName,
//                description = state.recipientUsername,
//                isDeletable = true,
//                onDeleteClick = { removeRecipient() }
//            )
//        }
    }
}

@Preview
@Composable
private fun PreviewRecipientSettingsScreen() {
    SMSRelayTheme {
        Surface {
            RecipientSettingsScreen(state = RecipientSettingsScreenState(),
                onAction = {},
                onBackClick = {})
        }
    }
}
