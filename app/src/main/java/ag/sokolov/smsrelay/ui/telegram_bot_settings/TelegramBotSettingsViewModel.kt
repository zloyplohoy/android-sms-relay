package ag.sokolov.smsrelay.ui.telegram_bot_settings

import ag.sokolov.smsrelay.domain.use_case.delete_telegram_bot_api_token.DeleteTelegramBotApiTokenUseCase
import ag.sokolov.smsrelay.domain.use_case.get_telegram_bot_username_flow.GetTelegramBotUsernameResultFlowUseCase
import ag.sokolov.smsrelay.domain.use_case.set_telegram_bot_api_token.SetTelegramBotApiTokenUseCase
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TelegramBotSettingsViewModel @Inject constructor(
    getTelegramBotUsernameResultFlowUseCase: GetTelegramBotUsernameResultFlowUseCase,
    private val setTelegramBotApiTokenUseCase: SetTelegramBotApiTokenUseCase,
    private val deleteTelegramBotApiTokenUseCase: DeleteTelegramBotApiTokenUseCase
) : ViewModel() {

    val state = mutableStateOf(TelegramBotSettingsScreenState())
    private val telegramBotUsernameResultFlow = getTelegramBotUsernameResultFlowUseCase()

    init {
        observeTelegramBotUsername()
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

    private fun observeTelegramBotUsername() {
        viewModelScope.launch {
            telegramBotUsernameResultFlow.collect { result ->
                state.value = state.value.copy(
                    botUsername = result.fold(
                        onSuccess = { "@$it" },
                        onFailure = { it.localizedMessage ?: "Unknown error" })
                )
            }
        }
    }
}
