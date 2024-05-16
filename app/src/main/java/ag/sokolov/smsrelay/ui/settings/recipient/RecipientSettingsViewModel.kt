package ag.sokolov.smsrelay.ui.settings.recipient

import ag.sokolov.smsrelay.domain.errors.TelegramBotException
import ag.sokolov.smsrelay.domain.models.TelegramUser
import ag.sokolov.smsrelay.domain.use_cases.get_telegram_recipient.GetTelegramRecipientUseCase
import ag.sokolov.smsrelay.domain.use_cases.is_telegram_installed_use_case.IsTelegramInstalledUseCase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipientSettingsViewModel @Inject constructor(
    private val getTelegramRecipientUseCase: GetTelegramRecipientUseCase,
    private val isTelegramInstalledUseCase: IsTelegramInstalledUseCase,
) : ViewModel() {
    var state by mutableStateOf<RecipientSettingsScreenState>(RecipientSettingsScreenState.Loading)
        private set

    init {
        observeTelegramRecipient()
    }

    fun onAction(action: RecipientSettingsAction) {
        when (action) {
            is RecipientSettingsAction.AddRecipient -> {

            }
            is RecipientSettingsAction.RemoveRecipient -> {
                // TODO: Delete recipient
            }
        }
    }

//    fun addRecipient(context: Context) {
//        val verificationCode = UUID.randomUUID().toString()
//
//        // Start workManager listener
//        // Generate validation code
//        // Launch intent
//        val url = ""
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//    }
//
//    private suspend fun generateTgLink(verificationCode: String): String {
////        val botUsername = getTelegramBotInfoResultFlowUseCase().first().getOrThrow()
////        return "tg://resolve?domain=${botUsername}&start=$verificationCode"
//        return ""
//    }

    private fun observeTelegramRecipient() {
        viewModelScope.launch {
            getTelegramRecipientUseCase().collect { telegramRecipientResult ->
                telegramRecipientResult.onSuccess { telegramRecipient ->
                    state = telegramRecipient?.let {
                        getRecipientConfiguredScreenState(it)
                    } ?: getRecipientNotConfiguredScreenState()
                }.onFailure { recipientError ->
                    state = getRecipientErrorScreenState(recipientError)
                }
            }
        }
    }

    private fun getRecipientConfiguredScreenState(telegramUser: TelegramUser) =
        RecipientSettingsScreenState.Configured(
            firstName = telegramUser.firstName,
            lastName = telegramUser.lastName,
            username = telegramUser.username
        )

    private fun getRecipientNotConfiguredScreenState() = if (isTelegramInstalledUseCase()) {
        RecipientSettingsScreenState.NotConfigured
    } else {
        RecipientSettingsScreenState.RecipientError(errorMessage = "Telegram must be installed")
    }

    private fun getRecipientErrorScreenState(error: Throwable) =
        when (error) {
            is TelegramBotException.BotApiTokenMissing -> RecipientSettingsScreenState.GenericError("Telegram bot must be configured first")
            is TelegramBotException.BotApiTokenInvalid -> RecipientSettingsScreenState.GenericError("Check Telegram bot settings")
            is TelegramBotException.NetworkUnavailable -> RecipientSettingsScreenState.GenericError("Network unavailable")
            is TelegramBotException.RecipientNotAllowed -> RecipientSettingsScreenState.RecipientError("Click to authorize recipient")
            else -> RecipientSettingsScreenState.RecipientError("Unhandled exception")
        }
}
