package ag.sokolov.smsrelay.ui.settings.recipient

import ag.sokolov.smsrelay.ui.common.MenuHeader
import ag.sokolov.smsrelay.ui.common.MenuItem
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RecipientSettingsScreen(
    state: RecipientSettingsScreenState,
    onAction: (RecipientSettingsAction) -> Unit,
    onBackClick: () -> Unit
) {
    Column {
        MenuHeader(title = "Recipient", onBackClick = { onBackClick() })
        MenuItem(icon = if (state is RecipientSettingsScreenState.NotConfigured) Icons.Filled.Add else null,
            title = when (state) {
                is RecipientSettingsScreenState.Loading -> "Loading..."
                is RecipientSettingsScreenState.NotConfigured -> "Add recipient"
                is RecipientSettingsScreenState.Configured -> state.lastName?.let { "${state.firstName} ${state.lastName}" }
                    ?: state.firstName

                is RecipientSettingsScreenState.GenericError, is RecipientSettingsScreenState.RecipientError -> "Error"
            },
            description = when (state) {
                is RecipientSettingsScreenState.Configured -> state.username
                is RecipientSettingsScreenState.GenericError -> state.errorMessage
                    ?: "Unhandled error"

                is RecipientSettingsScreenState.RecipientError -> state.errorMessage
                    ?: "Unhandled error"

                else -> null
            },
            onClick = when (state) {
                is RecipientSettingsScreenState.NotConfigured, is RecipientSettingsScreenState.RecipientError -> ({
                    onAction(
                        RecipientSettingsAction.AddRecipient
                    )
                })

                else -> null
            },
            extraIcon = when (state) {
                is RecipientSettingsScreenState.GenericError, is RecipientSettingsScreenState.RecipientError -> Icons.Filled.Warning
                is RecipientSettingsScreenState.Configured -> Icons.Filled.Clear
                else -> null
            },
            onExtraClick = if (state is RecipientSettingsScreenState.Configured) ({
                onAction(RecipientSettingsAction.RemoveRecipient)
            }) else null
        )
    }
}

@Preview
@Composable
private fun PreviewRecipientSettingsScreen() {
    SMSRelayTheme {
        Surface {
            RecipientSettingsScreen(state = RecipientSettingsScreenState.Loading,
                onAction = {},
                onBackClick = {})
        }
    }
}
