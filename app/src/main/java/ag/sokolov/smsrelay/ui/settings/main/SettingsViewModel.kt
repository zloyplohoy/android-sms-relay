package ag.sokolov.smsrelay.ui.settings.main

import ag.sokolov.smsrelay.domain.errors.DomainException
import ag.sokolov.smsrelay.domain.use_cases.get_telegram_bot_info.GetTelegramBotInfoUseCase
import ag.sokolov.smsrelay.ui.settings.bot.BotSettingsScreenState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getTelegramBotInfoUseCase: GetTelegramBotInfoUseCase
) : ViewModel() {

    var state by mutableStateOf(SettingsScreenState())
        private set

    init {
        observeTelegramBotUsername()
        observeTelegramRecipientName()
    }

    private fun observeTelegramBotUsername() {
        viewModelScope.launch {
            getTelegramBotInfoUseCase().collect { result ->
                result.onSuccess { telegramBot ->
                    state = state.copy(
                        botDescription = "@${telegramBot.username}",
                        showBotWarning = false
                    )
                }.onFailure { exception ->
                    when (exception) {
                        is DomainException.BotNotConfiguredException -> {
                            state = state.copy(
                                botDescription = "Not configured",
                                showBotWarning = false
                            )
                        }

                        is DomainException.InvalidBotApiTokenException -> {
                            state = state.copy(
                                botDescription = "API token invalid",
                                showBotWarning = true
                            )
                        }

                        else -> {
                            state = state.copy(
                                botDescription = "Unknown error",
                                showBotWarning = true
                            )
                        }
                    }
                }
            }
        }
    }


    private fun observeTelegramRecipientName() {
        viewModelScope.launch { }
    }
}
