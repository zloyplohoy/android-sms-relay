package ag.sokolov.smsrelay.ui.telegram_recipient_settings

import ag.sokolov.smsrelay.ui.common.ScreenTitle
import ag.sokolov.smsrelay.ui.common.SettingsItem
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TelegramRecipientSettingsScreen(
    viewModel: TelegramRecipientSettingsViewModel = hiltViewModel()
) {
    TelegramRecipientSettingsScreenContent(
        state = viewModel.state.value,
        addRecipient = viewModel::addRecipient
    )
}

@Composable
fun TelegramRecipientSettingsScreenContent(
    state: TelegramRecipientSettingsScreenState = TelegramRecipientSettingsScreenState(),
    addRecipient: (context: Context) -> Unit = {},
    removeRecipient: () -> Unit = {}
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ScreenTitle(title = "Recipient")
        Button(
            enabled = !state.isRecipientAdded,
            onClick = { addRecipient(context) },
            modifier = Modifier
                .padding(24.dp)
        ) {
            Text(text = "Use my Telegram account on this device")
        }
        if (state.isRecipientAdded) {
            SettingsItem(
                icon = Icons.Outlined.Person,
                title = state.recipientName,
                subtitle = state.recipientUsername,
                showDeleteButton = true,
                onDeleteClick = { removeRecipient() }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewTelegramRecipientSettingsScreenContent() {
    SMSRelayTheme {
        Surface {
            TelegramRecipientSettingsScreenContent(
                state = TelegramRecipientSettingsScreenState()
            )
        }
    }
}

@Preview
@PreviewLightDark
@Composable
private fun PreviewTelegramRecipientSettingsScreenContentRecipientAdded() {
    SMSRelayTheme {
        Surface {
            TelegramRecipientSettingsScreenContent(
                state = TelegramRecipientSettingsScreenState(
                    isRecipientAdded = true,
                    recipientName = "Konstantin Konstantinopolsky",
                    recipientUsername = "@konstantinopolsy_konstantin"
                )
            )
        }
    }
}
