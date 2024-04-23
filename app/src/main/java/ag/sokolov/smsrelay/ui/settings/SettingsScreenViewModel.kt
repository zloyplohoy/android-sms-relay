package ag.sokolov.smsrelay.ui.settings

import ag.sokolov.smsrelay.domain.use_case.get_telegram_bot_username_flow.GetTelegramBotUsernameResultFlowUseCase
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    getTelegramBotUsernameResultFlowUseCase: GetTelegramBotUsernameResultFlowUseCase
): ViewModel() {
    val state = mutableStateOf(SettingsScreenState())

    private val telegramBotUsernameResultFlow = getTelegramBotUsernameResultFlowUseCase()

    init {
        observeTelegramBotUsername()
    }

    private fun observeTelegramBotUsername() {
        viewModelScope.launch {
            telegramBotUsernameResultFlow.collect { result ->
                state.value = state.value.copy(
                    botConfiguration = result.fold(
                        onSuccess = { "@$it" },
                        onFailure = { it.localizedMessage ?: "Unknown error" }
                    )
                )
            }
        }
    }
}
