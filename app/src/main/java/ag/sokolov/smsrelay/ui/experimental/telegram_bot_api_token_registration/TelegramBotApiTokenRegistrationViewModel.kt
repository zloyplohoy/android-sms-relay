package ag.sokolov.smsrelay.ui.experimental.telegram_bot_api_token_registration

import ag.sokolov.smsrelay.domain.use_case.get_telegram_bot_api_token_flow.GetTelegramBotApiTokenFlowUseCase
import ag.sokolov.smsrelay.domain.use_case.set_telegram_bot_api_token.SetTelegramBotApiTokenUseCase
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TelegramBotApiTokenRegistrationViewModel @Inject constructor(
    getTelegramBotApiTokenFlowUseCase: GetTelegramBotApiTokenFlowUseCase,
    private val setTelegramBotApiTokenUseCase: SetTelegramBotApiTokenUseCase
): ViewModel() {
    val tokenFlow : Flow<String> = getTelegramBotApiTokenFlowUseCase().filterNotNull()

    var tokenTextField = mutableStateOf("")
        private set

    fun onTokenTextFieldValueChange(value: String) {
        tokenTextField.value = value
    }

    fun setTelegramBotApiToken() {
        viewModelScope.launch {
            setTelegramBotApiTokenUseCase(tokenTextField.value)
        }
    }
}
