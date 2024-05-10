package ag.sokolov.smsrelay.ui.settings.bot

import ag.sokolov.smsrelay.domain.errors.DomainException
import ag.sokolov.smsrelay.domain.use_cases.add_telegram_bot.AddTelegramBotUseCase
import ag.sokolov.smsrelay.domain.use_cases.get_telegram_bot_info.GetTelegramBotInfoUseCase
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
    private val getTelegramBotInfoUseCase: GetTelegramBotInfoUseCase,
    private val removeTelegramBotUseCase: RemoveTelegramBotUseCase,
    private val addTelegramBotUseCase: AddTelegramBotUseCase
) : ViewModel() {

    var state by mutableStateOf(BotSettingsScreenState())
        private set

    init {
        observeTelegramBotUsername()
    }

    fun onAction(action: BotSettingsAction) {
        when (action) {
            is BotSettingsAction.ToggleTokenDialog -> toggleTokenDialog()
            is BotSettingsAction.AddBot -> addBot(action.botApiToken)
            is BotSettingsAction.RemoveBot -> removeBot()
        }
    }

    private fun removeBot() {
        viewModelScope.launch {
            removeTelegramBotUseCase()
        }
    }

    private fun addBot(token: String) {
        state = BotSettingsScreenState()
        viewModelScope.launch {
            addTelegramBotUseCase(token)
        }
    }

    private fun toggleTokenDialog() {
        state = state.copy(showTokenDialog = !state.showTokenDialog)
    }

    private fun observeTelegramBotUsername() {
        viewModelScope.launch {
            getTelegramBotInfoUseCase().collect { result ->
                result.onSuccess { telegramBot ->
                    state = BotSettingsScreenState(
                        isBotConfigured = true,
                        botTitle = telegramBot.name,
                        botDescription = "@${telegramBot.username}",
                        showDeleteButton = true
                    )
                }.onFailure { exception ->
                    when (exception) {
                        is DomainException.BotNotConfiguredException -> {
                            state = BotSettingsScreenState(
                                isBotConfigured = false, botTitle = "Add a Telegram bot"
                            )
                        }

                        is DomainException.InvalidBotApiTokenException -> {
                            state = BotSettingsScreenState(
                                isBotConfigured = true,
                                botTitle = "API token invalid",
                                botDescription = "Click to update",
                                showWarning = true
                            )
                        }

                        else -> {
                            state = BotSettingsScreenState(
                                isBotConfigured = true,
                                botTitle = "Error",
                                botDescription = "Unhandled exception",
                                showWarning = true
                            )
                        }
                    }
                }
            }
        }
    }
}
