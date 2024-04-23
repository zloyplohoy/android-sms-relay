package ag.sokolov.smsrelay.ui.telegram_bot_settings

import ag.sokolov.smsrelay.ui.common.ScreenTitle
import ag.sokolov.smsrelay.ui.common.SettingsItem
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TelegramBotSettingsScreen(
    viewModel: TelegramBotSettingsViewModel = hiltViewModel()
) {
    TelegramBotSettingsScreenContent(
        state = viewModel.screenState.value,
        toggleTokenDialog = viewModel::toggleTokenDialog,
        deleteBot = viewModel::deleteBot
    )
    TelegramBotApiTokenDialog(
        state = viewModel.dialogState.value,
        toggleDialog = viewModel::toggleTokenDialog,
        onTokenTextFieldValueChange = viewModel::onTokenTextFieldValueChange,
        saveToken = viewModel::saveToken
    )
}

@Composable
fun TelegramBotSettingsScreenContent(
    state: TelegramBotSettingsScreenState = TelegramBotSettingsScreenState(),
    toggleTokenDialog: () -> Unit = {},
    deleteBot: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ScreenTitle(title = "Telegram bot")
        Button(
            enabled = !state.isBotRegistered,
            onClick = { toggleTokenDialog() },
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "Add bot with API key")
        }
        if(state.isBotRegistered){
            SettingsItem(
                icon = Icons.AutoMirrored.Outlined.Send,
                title = state.botName,
                subtitle = state.botUsername,
                showDeleteButton = true,
                onDeleteClick = { deleteBot() }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewTelegramBotSettingsScreenContent() {
    SMSRelayTheme {
        Surface {
            TelegramBotSettingsScreenContent()
        }
    }
}

@Preview
@Composable
private fun PreviewTelegramBotSettingsScreenContentWithBotRegistered() {
    SMSRelayTheme {
        Surface {
            TelegramBotSettingsScreenContent(
                state = TelegramBotSettingsScreenState(
                    isBotRegistered = true,
                    botName = "Awesome SMS relay bot",
                    botUsername = "@awesome_sms_relay_bot"
                )
            )
        }
    }
}
