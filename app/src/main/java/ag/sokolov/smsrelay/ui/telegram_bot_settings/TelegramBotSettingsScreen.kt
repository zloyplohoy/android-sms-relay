package ag.sokolov.smsrelay.ui.telegram_bot_settings

import ag.sokolov.smsrelay.ui.common.ScreenTitle
import ag.sokolov.smsrelay.ui.common.SettingsItem
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.Button
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
            onClick = { toggleTokenDialog() },
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "Open dialog")
        }
        if(state.isBotRegistered){
            SettingsItem(
                icon = Icons.AutoMirrored.Outlined.Send,
                title = state.botName,
                subtitle = "@${state.botUsername}",
                showDeleteButton = true,
                onDeleteClick = { deleteBot() }
            )
        }
    }
}

// navigateToRoute: (String) -> Unit = {},

//        navController: NavController

//    val context = LocalContext.current
//        Button(onClick = {
////            val uri = Uri.parse("https://t.me/botfather?newbot")
//            val uri = Uri.parse("tg://resolve?domain=botfather&start")
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//
//
//            context.startActivity(intent)
//        }) {
//            Text(text = "Go see botfather")
//        }


@Preview
@Composable
private fun PreviewTelegramBotSettingsScreenContent() {
    TelegramBotSettingsScreenContent()
}

@Preview
@Composable
private fun PreviewTelegramBotSettingsScreenContentWithBotRegistered() {
    TelegramBotSettingsScreenContent(
        state = TelegramBotSettingsScreenState(
            isBotRegistered = true,
            botName = "Awesome SMS relay bot",
            botUsername = "awesome_sms_relay_bot"
        )
    )
}
