package ag.sokolov.smsrelay.ui.telegram_bot_settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun TelegramBotSettingsScreen(
    viewModel: TelegramBotSettingsViewModel = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.state.value
    Column {
        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Back")
        }
        OutlinedTextField(value = state.tokenTextFieldValue, onValueChange = { value: String ->
            viewModel.onTokenTextFieldValueChange(value)
        })
        Button(onClick = { viewModel.setTelegramBotApiToken() }) {
            Text(text = "Set Telegram bot API token")
        }
        Text(text = state.botDetails)
        Button(onClick = { viewModel.deleteTelegramBotApiToken() }) {
            Text(text = "Delete Telegram bot API token")
        }
    }
}
