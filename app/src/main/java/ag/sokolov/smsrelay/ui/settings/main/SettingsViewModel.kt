package ag.sokolov.smsrelay.ui.settings.main

import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.use_case.get_telegram_bot.GetTelegramBotUseCase
import ag.sokolov.smsrelay.domain.use_case.get_telegram_recipient.GetTelegramRecipientUseCase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
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
            getTelegramBotUseCase().collect { response ->
                when (response) {
                    is Response.Success ->
                        state =
                            state.copy(
                                showBotWarning = false,
                                allowRecipientConfiguration = true,
                                botStatusDescription = response.data.name)
                    is Response.Failure ->
                        state =
                            state.copy(
                                showBotWarning = response.error !is DomainError.BotApiTokenMissing,
                                allowRecipientConfiguration = false,
                                botStatusDescription =
                                    when (response.error) {
                                        is DomainError.BotApiTokenInvalid -> "Invalid API token"
                                        is DomainError.NetworkUnavailable -> "Network unavailable"
                                        is DomainError.BotApiTokenMissing -> "Not configured"
                                        else -> "Unhandled error"
                                    })
                }
            }
        }
    }

    private fun observeTelegramRecipient() {
        viewModelScope.launch {
            getTelegramRecipientUseCase().collect { response ->
                when (response) {
                    is Response.Success ->
                        state =
                            state.copy(
                                allowRecipientConfiguration = true,
                                showBotWarning = false,
                                recipientStatusDescription =
                                    response.data.lastName?.let {
                                        "${response.data.firstName} ${response.data.lastName}"
                                    } ?: response.data.firstName)
                    is Response.Failure ->
                        state =
                            state.copy(
                                allowRecipientConfiguration =
                                    response.error !is DomainError.BotApiTokenMissing,
                                showRecipientWarning =
                                    response.error !is DomainError.RecipientIdMissing,
                                recipientStatusDescription =
                                    when (response.error) {
                                        is DomainError.BotApiTokenMissing ->
                                            "Configure the bot first"
                                        is DomainError.NetworkUnavailable -> "Network unavailable"
                                        is DomainError.BotApiTokenInvalid ->
                                            "Check Telegram bot settings"
                                        is DomainError.RecipientIdMissing -> "Not configured"
                                        is DomainError.RecipientNotAllowed -> "Validation required"
                                        else -> "Unhandled error"
                                    })
                }
            }
        }
    }
}
