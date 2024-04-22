package ag.sokolov.smsrelay.ui.experimental.telegram_bot_api_token_registration

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TelegramBotApiTokenRegistrationViewModel @Inject constructor(

): ViewModel() {
    var tokenTextField = mutableStateOf("")
        private set

    var tokenText = mutableStateOf("")
        private set

    fun onTokenTextFieldValueChange(value: String) {
        tokenTextField.value = value
    }

    fun setTelegramBotApiToken() {
        tokenText.value = tokenTextField.value
    }
}
