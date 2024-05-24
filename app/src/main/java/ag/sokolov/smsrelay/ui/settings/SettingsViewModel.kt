package ag.sokolov.smsrelay.ui.settings

import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.model.TelegramUser
import ag.sokolov.smsrelay.domain.use_case.add_telegram_bot.AddTelegramBotUseCase
import ag.sokolov.smsrelay.domain.use_case.delete_telegram_bot.DeleteTelegramBotUseCase
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
    private val getTelegramRecipientUseCase: GetTelegramRecipientUseCase,
    private val addTelegramBotUseCase: AddTelegramBotUseCase,
    private val deleteTelegramBotUseCase: DeleteTelegramBotUseCase
) : ViewModel() {

    var state by mutableStateOf(SettingsState())
        private set

    init {
        observeConfiguration()
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.AddTelegramBot -> addBot(action.botApiToken)
            is SettingsAction.RemoveTelegramBot -> removeBot()
        }
    }

    private fun addBot(botApiToken: String) {
        viewModelScope.launch { addTelegramBotUseCase(botApiToken) }
    }

    private fun removeBot() {
        viewModelScope.launch { deleteTelegramBotUseCase() }
    }

    private fun observeConfiguration() {
        viewModelScope.launch {
            combine(getTelegramBotUseCase(), getTelegramRecipientUseCase()) {
                    telegramBotResponse,
                    telegramRecipientResponse ->
                    telegramBotResponse to telegramRecipientResponse
                }
                .collect { (telegramBotResponse, telegramRecipientResponse) ->
                    updateState(telegramBotResponse, telegramRecipientResponse)
                }
        }
    }

    private fun updateState(
        telegramBotResponse: Response<TelegramBot, DomainError>,
        telegramRecipientResponse: Response<TelegramUser, DomainError>
    ) {
        state =
            state.copy(
                isLoading = false,
                recipientStatusDescription =
                    getRecipientStatusDescription(telegramRecipientResponse),
                showRecipientWarning = isRecipientWarningDisplayed(telegramRecipientResponse),
                botState = getBotState(telegramBotResponse))
    }

    private fun getRecipientStatusDescription(
        telegramRecipientResponse: Response<TelegramUser, DomainError>
    ): String =
        when (telegramRecipientResponse) {
            is Response.Success ->
                telegramRecipientResponse.data.lastName?.let {
                    "${telegramRecipientResponse.data.firstName} ${telegramRecipientResponse.data.lastName}"
                } ?: telegramRecipientResponse.data.firstName
            is Response.Failure ->
                when (telegramRecipientResponse.error) {
                    is DomainError.BotApiTokenMissing -> "Configure the bot first"
                    is DomainError.BotApiTokenInvalid -> "Check bot settings"
                    is DomainError.NetworkUnavailable -> "Network unavailable"
                    is DomainError.RecipientIdMissing -> "Not configured"
                    is DomainError.RecipientInvalid -> "Validation required"
                    else -> "Unhandled error"
                }
        }

    private fun isRecipientWarningDisplayed(
        telegramRecipientResponse: Response<TelegramUser, DomainError>
    ): Boolean =
        (telegramRecipientResponse is Response.Failure &&
            telegramRecipientResponse.error::class in
                setOf(
                    DomainError.NetworkUnavailable::class,
                    DomainError.RecipientInvalid::class,
                    DomainError.UnhandledError::class))

    private fun getBotState(telegramBotResponse: Response<TelegramBot, DomainError>): BotState =
        when (telegramBotResponse) {
            is Response.Success ->
                BotState.Configured(
                    botName = telegramBotResponse.data.name,
                    botUsername = telegramBotResponse.data.username)
            is Response.Failure ->
                when (telegramBotResponse.error) {
                    is DomainError.BotApiTokenMissing -> BotState.NotConfigured
                    is DomainError.BotApiTokenInvalid -> BotState.Error("Bot API token invalid")
                    else -> BotState.Error("Unhandled error")
                }
        }
}
