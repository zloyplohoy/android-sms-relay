package ag.sokolov.smsrelay.ui.telegram_bot_settings

import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun TelegramBotApiTokenDialog(
    state: TelegramBotApiTokenDialogState,
    toggleDialog: () -> Unit,
    onTokenTextFieldValueChange: (String) -> Unit,
    saveToken: (String) -> Unit
) {
    fun emptyTokenTextField() = onTokenTextFieldValueChange("")

    if (state.isDialogVisible) {
        Dialog(
            onDismissRequest = {
                toggleDialog()
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "API token",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp,
                            vertical = 16.dp
                        ),
                )
                TextField(
                    value = state.tokenTextFieldValue,
                    onValueChange = { value: String ->
                        onTokenTextFieldValueChange(value)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "CANCEL",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .clickable {
                                emptyTokenTextField()
                                toggleDialog()
                            }
                            .padding(16.dp)
                    )
                    Text(
                        text = "SAVE TOKEN",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (state.isTokenStructureValid) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                        },
                        modifier = Modifier
                            .then(
                                if (state.isTokenStructureValid) {
                                    Modifier.clickable {
                                        saveToken(state.tokenTextFieldValue)
                                        toggleDialog()
                                        emptyTokenTextField()
                                    }
                                } else {
                                    Modifier
                                }
                            )
                            .padding(16.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewTelegramBotApiTokenDialog() {
    SMSRelayTheme {
        Surface {
            TelegramBotApiTokenDialog(
                state = TelegramBotApiTokenDialogState(
                    isDialogVisible = true
                ),
                toggleDialog = {},
                onTokenTextFieldValueChange = {},
                saveToken = {}
            )
        }
    }
}

@Preview
@PreviewLightDark
@Composable
private fun PreviewTelegramBotApiTokenDialogTokenStructureValid() {
    SMSRelayTheme {
        Surface {
            TelegramBotApiTokenDialog(
                state = TelegramBotApiTokenDialogState(
                    isDialogVisible = true,
                    isTokenStructureValid = true,
                    tokenTextFieldValue = "123456:ABC-DEF1234ghIkl-zyx57W2v1u123ew11"
                ),
                toggleDialog = {},
                onTokenTextFieldValueChange = {},
                saveToken = {}
            )
        }
    }
}
