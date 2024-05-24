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
import kotlinx.coroutines.flow.combine
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
        observeConfiguration()
    }

    private fun observeConfiguration() {
        viewModelScope.launch {
            combine(getTelegramBotUseCase(), getTelegramRecipientUseCase()) {
                    telegramBotResponse,
                    telegramRecipientResponse ->
                    telegramBotResponse to telegramRecipientResponse
                }
                .collect { (telegramBotResponse, telegramRecipientResponse) ->
                    state =
                        state.copy(
                            botStatusDescription =
                                when (telegramBotResponse) {
                                    is Response.Success -> "@${telegramBotResponse.data.username}"
                                    is Response.Failure ->
                                        when (telegramBotResponse.error) {
                                            is DomainError.BotApiTokenMissing -> "Not configured"
                                            is DomainError.BotApiTokenInvalid -> "Invalid API token"
                                            is DomainError.NetworkUnavailable ->
                                                "Network unavailable"
                                            else -> "Unhandled error"
                                        }
                                },
                            showBotWarning =
                                (telegramBotResponse is Response.Failure &&
                                    telegramBotResponse.error !is DomainError.BotApiTokenMissing),
                            recipientStatusDescription =
                                when (telegramRecipientResponse) {
                                    is Response.Success ->
                                        telegramRecipientResponse.data.lastName?.let {
                                            "${telegramRecipientResponse.data.firstName} ${telegramRecipientResponse.data.lastName}"
                                        } ?: telegramRecipientResponse.data.firstName
                                    is Response.Failure ->
                                        when (telegramRecipientResponse.error) {
                                            is DomainError.BotApiTokenMissing ->
                                                "Configure the bot first"
                                            is DomainError.BotApiTokenInvalid ->
                                                "Check bot settings"
                                            is DomainError.NetworkUnavailable ->
                                                "Network unavailable"
                                            is DomainError.RecipientIdMissing -> "Not configured"
                                            is DomainError.RecipientNotAllowed ->
                                                "Validation required"
                                            else -> "Unhandled error"
                                        }
                                },
                            showRecipientWarning =
                                (telegramRecipientResponse is Response.Failure &&
                                    telegramRecipientResponse.error::class in
                                        setOf(
                                            DomainError.NetworkUnavailable::class,
                                            DomainError.RecipientNotAllowed::class,
                                            DomainError.UnhandledError::class)))
                }
        }
    }
}
