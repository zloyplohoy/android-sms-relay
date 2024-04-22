package ag.sokolov.smsrelay.ui.experimental.telegram_bot_api_token_registration

import ag.sokolov.smsrelay.domain.use_case.delete_telegram_bot_api_token.DeleteTelegramBotApiTokenUseCase
import ag.sokolov.smsrelay.domain.use_case.get_telegram_bot_api_token_flow.GetTelegramBotApiTokenFlowUseCase
import ag.sokolov.smsrelay.domain.use_case.set_telegram_bot_api_token.SetTelegramBotApiTokenUseCase
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TelegramBotApiTokenRegistrationViewModel @Inject constructor(
    getTelegramBotApiTokenFlowUseCase: GetTelegramBotApiTokenFlowUseCase,
    private val setTelegramBotApiTokenUseCase: SetTelegramBotApiTokenUseCase,
    private val deleteTelegramBotApiTokenUseCase: DeleteTelegramBotApiTokenUseCase
) : ViewModel() {

    val state = mutableStateOf(TelegramBotApiTokenRegistrationScreenState())
    private val telegramBotApiTokenFlow: Flow<String?> = getTelegramBotApiTokenFlowUseCase()

    init {
        observeTelegramBotApiToken()
    }

    fun onTokenTextFieldValueChange(value: String) {
        state.value = state.value.copy(tokenTextFieldValue = value)
    }

    fun setTelegramBotApiToken() {
        viewModelScope.launch {
            setTelegramBotApiTokenUseCase(state.value.tokenTextFieldValue)
        }
    }

    fun deleteTelegramBotApiToken() {
        viewModelScope.launch {
            deleteTelegramBotApiTokenUseCase()
        }
    }

    private fun observeTelegramBotApiToken() {
        viewModelScope.launch {
            telegramBotApiTokenFlow.collect { telegramBotApiToken ->
                state.value = state.value.copy(tokenValue = telegramBotApiToken?: "Token not set")
            }
        }
    }
}
