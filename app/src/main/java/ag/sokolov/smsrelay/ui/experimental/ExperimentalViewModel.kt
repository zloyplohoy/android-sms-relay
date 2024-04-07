package ag.sokolov.smsrelay.ui.experimental

import ag.sokolov.smsrelay.domain.use_case.get_telegram_bot_api_key.GetTelegramBotApiKeyUseCase
import ag.sokolov.smsrelay.domain.use_case.set_telegram_bot_api_key.SetTelegramBotApiKeyUseCase
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExperimentalViewModel @Inject constructor(
    private val setTelegramApiKeyUseCase: SetTelegramBotApiKeyUseCase,
    private val getTelegramBotApiKeyUseCase: GetTelegramBotApiKeyUseCase
) : ViewModel() {
    var apiKeyTextFieldValue = mutableStateOf("")
        private set

    var responseText = mutableStateOf("response")
        private set

    var responseText2 = mutableStateOf("response")
        private set

    fun onValueChange(value: String) {
        apiKeyTextFieldValue.value = value
    }

    fun onClick() {
        viewModelScope.launch {
            setTelegramApiKeyUseCase(apiKeyTextFieldValue.value).onSuccess {
                responseText.value = "Success"
            }.onFailure { exception ->
                responseText.value = exception.message ?: "Unknown error"
            }
        }
    }

    fun onClick2() {
        viewModelScope.launch {
            getTelegramBotApiKeyUseCase().onSuccess {
                responseText2.value = it ?: "Empty"
            }.onFailure { exception ->
                responseText2.value = exception.message ?: "Unknown error"
            }
        }
    }
}
