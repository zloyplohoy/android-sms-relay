package ag.sokolov.smsrelay.ui.telegram_bot_settings

import ag.sokolov.smsrelay.domain.use_cases.delete_telegram_bot_api_token.DeleteTelegramBotApiTokenUseCase
import ag.sokolov.smsrelay.domain.use_cases.get_telegram_bot_details_result_flow.GetTelegramBotDetailsResultFlowUseCase
import ag.sokolov.smsrelay.domain.use_cases.add_telegram_bot.AddTelegramBotUseCase
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TelegramBotSettingsViewModel @Inject constructor(
    getTelegramBotDetailsResultFlowUseCase: GetTelegramBotDetailsResultFlowUseCase,
    private val deleteTelegramBotApiTokenUseCase: DeleteTelegramBotApiTokenUseCase,
    private val addTelegramBotUseCase: AddTelegramBotUseCase
) : ViewModel() {

    private val botApiTokenRegex: Regex = """^\d+:[A-Za-z0-9_-]{35}$""".toRegex()
    private val telegramBotDetailsResultFlow = getTelegramBotDetailsResultFlowUseCase()

    val screenState = mutableStateOf(TelegramBotSettingsScreenState())
    val dialogState = mutableStateOf(TelegramBotApiTokenDialogState())

    init {
        observeTelegramBotUsername()
    }

    fun deleteBot() {
        viewModelScope.launch {
            deleteTelegramBotApiTokenUseCase()
        }
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            addTelegramBotUseCase(token)
        }
    }

    fun toggleTokenDialog() {
        dialogState.value = dialogState.value.copy(
            isDialogVisible = !dialogState.value.isDialogVisible
        )
    }

    fun onTokenTextFieldValueChange(value: String) {
        dialogState.value = dialogState.value.copy(
            tokenTextFieldValue = value,
            isTokenStructureValid = isTokenStructureValid(value)
        )
    }

    private fun isTokenStructureValid(token: String) = token.matches(botApiTokenRegex)

    private fun observeTelegramBotUsername() {
        viewModelScope.launch {
            telegramBotDetailsResultFlow.collect { result ->
                result.onSuccess { telegramBot ->
                        screenState.value = screenState.value.copy(
                            isBotRegistered = true,
                            botName = telegramBot.name,
                            botUsername = telegramBot.username
                        )
                    }.onFailure {exception ->
                        when (exception) {
                            is IllegalArgumentException -> {
                                screenState.value = screenState.value.copy(
                                    isBotRegistered = false
                                )
                            }
                        }
                    }
            }
        }
    }
}



//    fun onTokenTextFieldValueChange(value: String) {
//        state.value = state.value.copy(tokenTextFieldValue = value)
//    }
//
//    fun setTelegramBotApiToken() {
//        viewModelScope.launch {
//            setTelegramBotApiTokenUseCase(state.value.tokenTextFieldValue)
//        }
//    }
