package ag.sokolov.smsrelay.ui.settings.main

import ag.sokolov.smsrelay.domain.errors.DomainException
import ag.sokolov.smsrelay.domain.use_cases.get_telegram_bot.GetTelegramBotUseCase
import ag.sokolov.smsrelay.domain.use_cases.get_telegram_recipient.GetTelegramRecipientUseCase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: Clean up comments
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getTelegramBotUseCase: GetTelegramBotUseCase,
    private val getTelegramRecipientUseCase: GetTelegramRecipientUseCase
) : ViewModel() {

    var state by mutableStateOf(SettingsScreenState())
        private set

    init {
        observeTelegramBot()
        observeTelegramRecipient()
    }

    private fun observeTelegramBot() {
        viewModelScope.launch {
            getTelegramBotUseCase().collect { telegramBotResult ->
                telegramBotResult.onSuccess { telegramBot ->
                    state = telegramBot?.let {
                        state.copy(
                            botDescription = "@${it.username}", showBotWarning = false
                        )
                    } ?: state.copy(
                        botDescription = "Not configured", showBotWarning = false
                    )
                }.onFailure { telegramBotError ->
                    state = state.copy(
                        showBotWarning = true, botDescription = when (telegramBotError) {
                            is DomainException.InvalidBotApiTokenException -> "Invalid API token"
                            is DomainException.BotNetworkException -> "Network unavailable"
                            else -> "Unhandled error"
                        }
                    )
                }
            }
        }
    }

    private fun observeTelegramRecipient() {
        viewModelScope.launch {
            getTelegramRecipientUseCase().collect() { telegramRecipientResult ->
                telegramRecipientResult.onSuccess { telegramRecipient ->
                    state = telegramRecipient?.let {
                        state.copy(recipientConfiguration = telegramRecipient.lastName?.let { "${telegramRecipient.firstName} ${telegramRecipient.lastName}" }
                            ?: telegramRecipient.firstName)
                    } ?: state.copy(
                        recipientConfiguration = "Not configured"
                    )
                }.onFailure { telegramRecipientError ->
                    state = state.copy(
                        recipientConfiguration = when(telegramRecipientError) {
                            is DomainException.BotNotConfiguredException -> "Configure the bot first"
                            is DomainException.BotNetworkException -> "Network unavailable"
                            is DomainException.InvalidBotApiTokenException -> "Check Telegram bot settings"
                            else -> "Unhandled error"
                        }
                    )
                }
            }
        }
    }
}
