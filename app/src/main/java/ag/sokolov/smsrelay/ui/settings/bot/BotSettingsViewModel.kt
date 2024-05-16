package ag.sokolov.smsrelay.ui.settings.bot

import ag.sokolov.smsrelay.domain.errors.TelegramBotException
import ag.sokolov.smsrelay.domain.use_cases.add_telegram_bot.AddTelegramBotUseCase
import ag.sokolov.smsrelay.domain.use_cases.get_telegram_bot.GetTelegramBotUseCase
import ag.sokolov.smsrelay.domain.use_cases.remove_telegram_bot.RemoveTelegramBotUseCase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BotSettingsViewModel @Inject constructor(
    private val removeTelegramBotUseCase: RemoveTelegramBotUseCase,
    private val addTelegramBotUseCase: AddTelegramBotUseCase,
    private val getTelegramBotUseCase: GetTelegramBotUseCase
) : ViewModel() {

    var state by mutableStateOf<BotSettingsScreenState>(BotSettingsScreenState.Loading)
        private set

    init {
        observeTelegramBot()
    }

    fun onAction(action: BotSettingsAction) {
        when (action) {
            is BotSettingsAction.AddBot -> addBot(action.botApiToken)
            is BotSettingsAction.RemoveBot -> removeBot()
        }
    }

    private fun removeBot() {
        viewModelScope.launch {
            state = BotSettingsScreenState.NotConfigured
            removeTelegramBotUseCase()
        }
    }

    private fun addBot(token: String) {
        state = BotSettingsScreenState.Loading
        viewModelScope.launch {
            addTelegramBotUseCase(token)
        }
    }

    private fun observeTelegramBot() {
        viewModelScope.launch {
            getTelegramBotUseCase().collect { telegramBotResult ->
                telegramBotResult.onSuccess { telegramBot ->
                    state = telegramBot?.let {
                        BotSettingsScreenState.Configured(
                            botName = it.name,
                            botUsername = it.username
                        )
                    } ?: BotSettingsScreenState.NotConfigured
                }.onFailure { telegramBotError ->
                    state = BotSettingsScreenState.Error(
                        errorMessage = when(telegramBotError) {
                            is TelegramBotException.BotApiTokenInvalid -> "Invalid token, click to update"
                            is TelegramBotException.NetworkUnavailable -> "Network unavailable"
                            else -> "Unhandled error"
                        }
                    )
                }
            }
        }
    }
}
