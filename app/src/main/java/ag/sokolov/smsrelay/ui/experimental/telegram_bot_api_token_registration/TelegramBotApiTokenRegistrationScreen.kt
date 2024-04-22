package ag.sokolov.smsrelay.ui.experimental.telegram_bot_api_token_registration

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TelegramBotApiTokenRegistrationScreen(
    viewModel: TelegramBotApiTokenRegistrationViewModel = hiltViewModel()
) {
    Column {
        OutlinedTextField(value = viewModel.tokenTextField.value, onValueChange = { value: String ->
            viewModel.onTokenTextFieldValueChange(value)
        })
        Button(onClick = { viewModel.setTelegramBotApiToken() }) {
            Text(text = "Set Telegram bot API token")
        }
        Text(text = viewModel.tokenText.value)
    }
}
