package ag.sokolov.smsrelay.ui.telegram_bot_settings

import ag.sokolov.smsrelay.domain.use_case.delete_telegram_bot_api_token.DeleteTelegramBotApiTokenUseCase
import ag.sokolov.smsrelay.domain.use_case.get_telegram_bot_details_flow.GetTelegramBotDetailsResultFlowUseCase
import ag.sokolov.smsrelay.domain.use_case.set_telegram_bot_api_token.SetTelegramBotApiTokenUseCase
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TelegramBotSettingsViewModel @Inject constructor(
    getTelegramBotDetailsResultFlowUseCase: GetTelegramBotDetailsResultFlowUseCase,
    private val setTelegramBotApiTokenUseCase: SetTelegramBotApiTokenUseCase,
    private val deleteTelegramBotApiTokenUseCase: DeleteTelegramBotApiTokenUseCase
) : ViewModel() {

    val state = mutableStateOf(TelegramBotSettingsScreenState())
    private val telegramBotDetailsResultFlow = getTelegramBotDetailsResultFlowUseCase()

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
            telegramBotDetailsResultFlow.collect { telegramBotDetailsResult ->
                telegramBotDetailsResult.onSuccess { telegramBot ->
                    state.value = state.value.copy(botDetails = telegramBot.username)
                }.onFailure { telegramBotError ->
                    state.value = state.value.copy(
                        botDetails = telegramBotError.localizedMessage ?: "Unknown error"
                    )
                }

            }
        }
    }
}
