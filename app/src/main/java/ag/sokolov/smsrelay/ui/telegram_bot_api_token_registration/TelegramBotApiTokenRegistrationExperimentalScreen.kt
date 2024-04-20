package ag.sokolov.smsrelay.ui.telegram_bot_api_token_registration

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TelegramBotApiTokenRegistrationExperimentalScreen(
    viewModel: TelegramBotApiTokenRegistrationExperimentalViewModel = hiltViewModel()
) {
    Column {
        Text(text = "API Token Input")
        TextField(value = viewModel.apiKeyTextFieldValue.value,
            onValueChange = { value: String -> viewModel.onValueChange(value) })
        Button(onClick = { viewModel.onClick() }) {
            Text(text = "Submit")
        }
        Text(text = viewModel.responseText.value)
        Button(onClick = { viewModel.onClick2() }) {
            Text(text = "Retrieve")
        }
        Text(text = viewModel.responseText2.value)
    }
}
