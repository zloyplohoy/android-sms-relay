package ag.sokolov.smsrelay.ui.settings.bot

import ag.sokolov.smsrelay.ui.settings.SettingsAction
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun BotApiTokenDialog(toggleDialog: () -> Unit, onAction: (SettingsAction) -> Unit) {
    var tokenTextFieldValue by rememberSaveable { mutableStateOf("") }

    Dialog(onDismissRequest = { toggleDialog() }) {
        Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                text = "API token",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            )
            TextField(
                value = tokenTextFieldValue,
                onValueChange = { value: String -> tokenTextFieldValue = value },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                visualTransformation = VisualTransformation.None)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    text = "CANCEL",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.clickable { toggleDialog() }.padding(16.dp))
                Text(
                    text = "SAVE TOKEN",
                    style = MaterialTheme.typography.labelMedium,
                    modifier =
                        Modifier.clickable {
                                toggleDialog()
                                if (tokenTextFieldValue == "") {
                                    onAction(SettingsAction.RemoveTelegramBot)
                                } else {
                                    onAction(
                                        SettingsAction.AddTelegramBot(
                                            botApiToken = tokenTextFieldValue))
                                }
                            }
                            .padding(16.dp))
            }
        }
    }
}

@Preview
@PreviewLightDark
@Composable
private fun PreviewTelegramBotApiTokenDialog() {
    SMSRelayTheme { Surface { BotApiTokenDialog(onAction = {}, toggleDialog = {}) } }
}
