package ag.sokolov.smsrelay.ui.settings

import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.model.TelegramUser
import ag.sokolov.smsrelay.domain.use_case.add_telegram_bot.AddTelegramBotUseCase
import ag.sokolov.smsrelay.domain.use_case.add_telegram_recipient.AddTelegramRecipientUseCase
import ag.sokolov.smsrelay.domain.use_case.delete_telegram_bot.DeleteTelegramBotUseCase
import ag.sokolov.smsrelay.domain.use_case.get_telegram_bot.GetTelegramBotUseCase
import ag.sokolov.smsrelay.domain.use_case.get_telegram_recipient.GetTelegramRecipientUseCase
import ag.sokolov.smsrelay.ui.settings.action.SettingsAction
import ag.sokolov.smsrelay.ui.settings.state.BotState
import ag.sokolov.smsrelay.ui.settings.state.RecipientState
import ag.sokolov.smsrelay.ui.settings.state.SettingsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
    private val getTelegramBotUseCase: GetTelegramBotUseCase,
    private val getTelegramRecipientUseCase: GetTelegramRecipientUseCase,
    private val addTelegramBotUseCase: AddTelegramBotUseCase,
    private val deleteTelegramBotUseCase: DeleteTelegramBotUseCase,
    private val addTelegramRecipientUseCase: AddTelegramRecipientUseCase
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
            is SettingsAction.AddRecipient -> viewModelScope.launch { addTelegramRecipientUseCase() }
            is SettingsAction.RemoveRecipient -> Unit
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
        telegramBotResponse: Response<TelegramBot?, DomainError>,
        telegramRecipientResponse: Response<TelegramUser?, DomainError>
    ) {
        state =
            state.copy(
                isLoading = false,
                botState = getBotState(telegramBotResponse),
                recipientState = getRecipientState(telegramRecipientResponse))
    }

    private fun getBotState(telegramBotResponse: Response<TelegramBot?, DomainError>): BotState =
        when (telegramBotResponse) {
            is Response.Success ->
                telegramBotResponse.data?.let { telegramBot ->
                    BotState.Configured(
                        botName = telegramBot.name, botUsername = telegramBot.username)
                } ?: BotState.NotConfigured
            is Response.Failure ->
                when (telegramBotResponse.error) {
                    is DomainError.NetworkUnavailable -> BotState.Error("Network unavailable")
                    is DomainError.BotApiTokenInvalid -> BotState.Error("Bot API token invalid")
                    else -> BotState.Error("Unhandled error")
                }
        }

    private fun getRecipientState(
        telegramRecipientResponse: Response<TelegramUser?, DomainError>
    ): RecipientState =
        when (telegramRecipientResponse) {
            is Response.Success ->
                telegramRecipientResponse.data?.let { telegramRecipient ->
                    RecipientState.Configured(
                        fullName = "Aleksei", username = telegramRecipient.username)
                } ?: RecipientState.NotConfigured
            is Response.Failure ->
                when (telegramRecipientResponse.error) {
                    is DomainError.NetworkUnavailable,
                    DomainError.BotApiTokenInvalid ->
                        RecipientState.BotError("Check bot settings")
                    is DomainError.RecipientInvalid ->
                        RecipientState.RecipientError("Recipient blocked the bot")
                    else -> RecipientState.RecipientError("Unhandled error")
                }
        }
}
