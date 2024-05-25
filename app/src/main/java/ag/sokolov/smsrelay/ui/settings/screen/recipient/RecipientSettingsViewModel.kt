package ag.sokolov.smsrelay.ui.settings.screen.recipient

import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramUser
import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import ag.sokolov.smsrelay.domain.use_case.add_telegram_recipient.AddTelegramRecipientUseCase
import ag.sokolov.smsrelay.domain.use_case.get_telegram_recipient.GetTelegramRecipientUseCase
import ag.sokolov.smsrelay.domain.use_case.is_telegram_installed_use_case.IsTelegramInstalledUseCase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class RecipientSettingsViewModel
@Inject
constructor(
    private val addTelegramRecipientUseCase: AddTelegramRecipientUseCase,
    private val isTelegramInstalledUseCase: IsTelegramInstalledUseCase,
    private val configurationRepository: ConfigurationRepository,
    private val getTelegramRecipientUseCase: GetTelegramRecipientUseCase
) : ViewModel() {

    var state by mutableStateOf<RecipientSettingsScreenState>(RecipientSettingsScreenState.Loading)
        private set

    init {
        observeTelegramRecipient()
    }

    fun onAction(action: RecipientSettingsAction) {
        when (action) {
            is RecipientSettingsAction.AddRecipient -> {
                viewModelScope.launch { addTelegramRecipientUseCase() }
            }
            is RecipientSettingsAction.RemoveRecipient -> {
                viewModelScope.launch { configurationRepository.deleteTelegramRecipientId() }
            }
        }
    }

    private fun observeTelegramRecipient() {
        viewModelScope.launch {
            getTelegramRecipientUseCase().collect { response ->
                state =
                    when (response) {
                        is Response.Success -> getRecipientConfiguredScreenState(response.data)
                        is Response.Failure -> getRecipientErrorScreenState(response.error)
                    }
            }
        }
    }

    private fun getRecipientConfiguredScreenState(telegramRecipient: TelegramUser?) =
        telegramRecipient?.let {
            RecipientSettingsScreenState.Configured(
                firstName = it.firstName, lastName = it.lastName, username = it.username
            )
        }
            ?: if (isTelegramInstalledUseCase()) {
                RecipientSettingsScreenState.NotConfigured
            } else {
                RecipientSettingsScreenState.RecipientError(
                    errorMessage = "Telegram must be installed"
                )
            }

    private fun getRecipientErrorScreenState(exception: DomainError) =
        when (exception) {
            is DomainError.BotApiTokenInvalid -> RecipientSettingsScreenState.GenericError("Check Telegram bot settings")
            is DomainError.NetworkUnavailable -> RecipientSettingsScreenState.GenericError("Network unavailable")
            is DomainError.RecipientInvalid -> RecipientSettingsScreenState.RecipientError("Click to re-register recipient")
            else -> RecipientSettingsScreenState.RecipientError("Unhandled exception")
        }
}
