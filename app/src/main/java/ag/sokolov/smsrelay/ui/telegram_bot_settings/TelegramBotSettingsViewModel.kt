package ag.sokolov.smsrelay.ui.telegram_bot_settings

import ag.sokolov.smsrelay.domain.errors.DomainException
import ag.sokolov.smsrelay.domain.use_cases.add_telegram_bot.AddTelegramBotUseCase
import ag.sokolov.smsrelay.domain.use_cases.get_telegram_bot_info_result_flow.GetTelegramBotInfoResultFlowUseCase
import ag.sokolov.smsrelay.domain.use_cases.remove_telegram_bot.RemoveTelegramBotUseCase
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TelegramBotSettingsViewModel @Inject constructor(
    getTelegramBotInfoResultFlowUseCase: GetTelegramBotInfoResultFlowUseCase,
    private val removeTelegramBotUseCase: RemoveTelegramBotUseCase,
    private val addTelegramBotUseCase: AddTelegramBotUseCase
) : ViewModel() {

    private val botApiTokenRegex: Regex = """^\d+:[A-Za-z0-9_-]{35}$""".toRegex()
    private val telegramBotInfoResultFlow = getTelegramBotInfoResultFlowUseCase()

    val screenState = mutableStateOf(TelegramBotSettingsScreenState())
    val dialogState = mutableStateOf(TelegramBotApiTokenDialogState())

    init {
        observeTelegramBotUsername()
    }

    fun removeBot() {
        viewModelScope.launch {
            removeTelegramBotUseCase()
        }
    }

    fun addBot(token: String) {
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
            telegramBotInfoResultFlow.collect { result ->
                result.onSuccess { telegramBot ->
                    screenState.value = screenState.value.copy(
                        isBotAdded = true,
                        botName = telegramBot.name,
                        botUsername = "@${telegramBot.username}"
                    )
                }.onFailure { exception ->
                    when (exception) {
                        is DomainException.BotNotFoundException -> {
                            screenState.value = screenState.value.copy(
                                isBotAdded = false
                            )
                        }

                        else -> {
                            screenState.value = screenState.value.copy(
                                isBotAdded = true,
                                botName = "Bot experiencing errors",
                                botUsername = exception.localizedMessage
                                    ?: "Error: Unhandled exception"
                            )
                        }
                    }
                }
            }
        }
    }
}
