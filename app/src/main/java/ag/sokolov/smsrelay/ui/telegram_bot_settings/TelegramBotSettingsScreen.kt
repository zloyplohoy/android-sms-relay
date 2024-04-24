package ag.sokolov.smsrelay.ui.telegram_bot_settings

import ag.sokolov.smsrelay.ui.common.ScreenTitle
import ag.sokolov.smsrelay.ui.common.SettingsItem
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TelegramBotSettingsScreen(
    viewModel: TelegramBotSettingsViewModel = hiltViewModel()
) {
    TelegramBotSettingsScreenContent(
        state = viewModel.screenState.value,
        toggleTokenDialog = viewModel::toggleTokenDialog,
        removeBot = viewModel::removeBot
    )
    TelegramBotApiTokenDialog(
        state = viewModel.dialogState.value,
        toggleDialog = viewModel::toggleTokenDialog,
        onTokenTextFieldValueChange = viewModel::onTokenTextFieldValueChange,
        addBot = viewModel::addBot
    )
}

@Composable
fun TelegramBotSettingsScreenContent(
    state: TelegramBotSettingsScreenState = TelegramBotSettingsScreenState.Loading,
    toggleTokenDialog: () -> Unit = {},
    removeBot: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ScreenTitle(title = "Telegram bot")
        SettingsItem(
            icon = Icons.AutoMirrored.Outlined.Send,
            title = state.botTitle,
            description = state.botDescription,
            isClickable = !state.isBotAdded,
            onClick = { toggleTokenDialog() },
            isDeletable = state.isBotAdded,
            onDeleteClick = { removeBot() }
        )
    }
}

@Preview
@Composable
private fun PreviewTelegramBotSettingsScreenContent() {
    SMSRelayTheme {
        Surface {
            TelegramBotSettingsScreenContent(
                state = TelegramBotSettingsScreenState.Loading
            )
        }
    }
}

@Preview
@Composable
private fun PreviewTelegramBotSettingsScreenContentLoading() {
    SMSRelayTheme {
        Surface {
            TelegramBotSettingsScreenContent(
                state = TelegramBotSettingsScreenState.NotConfigured
            )
        }
    }
}

@Preview
@Composable
private fun PreviewTelegramBotSettingsScreenContentBotAdded() {
    SMSRelayTheme {
        Surface {
            TelegramBotSettingsScreenContent(
                state = TelegramBotSettingsScreenState.Configured(
                    isBotAdded = true,
                    botTitle = "Awesome SMS relay bot",
                    botDescription = "@awesome_sms_relay_bot"
                )
            )
        }
    }
}
