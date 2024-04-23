package ag.sokolov.smsrelay.ui.settings

import ag.sokolov.smsrelay.domain.use_cases.get_telegram_bot_info_result_flow.GetTelegramBotInfoResultFlowUseCase
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    getTelegramBotInfoResultFlowUseCase: GetTelegramBotInfoResultFlowUseCase
): ViewModel() {
    val state = mutableStateOf(SettingsScreenState())

    private val telegramBotUsernameResultFlow = getTelegramBotInfoResultFlowUseCase()

    init {
        observeTelegramBotUsername()
    }

    private fun observeTelegramBotUsername() {
        viewModelScope.launch {
            telegramBotUsernameResultFlow.collect { result ->
                state.value = state.value.copy(
                    botConfiguration = result.fold(
                        onSuccess = { "@${it.username}" },
                        onFailure = { it.localizedMessage ?: "Unhandled exception" }
                    )
                )
            }
        }
    }
}
