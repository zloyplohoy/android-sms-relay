package ag.sokolov.smsrelay.ui.settings

import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.model.TelegramUser
import ag.sokolov.smsrelay.domain.use_case.add_telegram_bot.AddTelegramBotUseCase
import ag.sokolov.smsrelay.domain.use_case.add_telegram_recipient.AddTelegramRecipientUseCase
import ag.sokolov.smsrelay.domain.use_case.delete_telegram_bot.DeleteTelegramBotUseCase
import ag.sokolov.smsrelay.domain.use_case.delete_telegram_recipient.DeleteTelegramRecipientUseCase
import ag.sokolov.smsrelay.domain.use_case.get_telegram_bot.GetTelegramBotUseCase
import ag.sokolov.smsrelay.domain.use_case.get_telegram_recipient.GetTelegramRecipientUseCase
import ag.sokolov.smsrelay.domain.use_case.is_online.IsOnlineUseCase
import ag.sokolov.smsrelay.ui.settings.action.SettingsAction
import ag.sokolov.smsrelay.ui.settings.state.BotState
import ag.sokolov.smsrelay.ui.settings.state.RecipientState
import ag.sokolov.smsrelay.ui.settings.state.SettingsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
    private val isOnlineUseCase: IsOnlineUseCase,
    private val getTelegramBotUseCase: GetTelegramBotUseCase,
    private val getTelegramRecipientUseCase: GetTelegramRecipientUseCase,
    private val addTelegramBotUseCase: AddTelegramBotUseCase,
    private val deleteTelegramBotUseCase: DeleteTelegramBotUseCase,
    private val addTelegramRecipientUseCase: AddTelegramRecipientUseCase,
    private val deleteTelegramRecipientUseCase: DeleteTelegramRecipientUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state

    init {
        observeConfiguration()
    }

    private fun observeConfiguration() {
        viewModelScope.launch {
            combine(isOnlineUseCase(), getTelegramBotUseCase(), getTelegramRecipientUseCase()) {
                    isOnline,
                    telegramBotResponse,
                    telegramRecipientResponse ->
                    Triple(isOnline, telegramBotResponse, telegramRecipientResponse)
                }
                .collect { (isOnline, telegramBotResponse, telegramRecipientResponse) ->
                    updateState(isOnline, telegramBotResponse, telegramRecipientResponse)
                }
        }
    }

    private fun updateState(
        isOnline: Boolean,
        telegramBotResponse: Response<TelegramBot?, DomainError>,
        telegramRecipientResponse: Response<TelegramUser?, DomainError>
    ) {
        _state.value =
            _state.value.copy(
                isLoading = isLoading(isOnline, telegramBotResponse, telegramRecipientResponse),
                botState = getBotState(telegramBotResponse),
                recipientState = getRecipientState(telegramRecipientResponse))
    }

    private fun isLoading(
        isOnline: Boolean,
        telegramBotResponse: Response<TelegramBot?, DomainError>,
        telegramRecipientResponse: Response<TelegramUser?, DomainError>
    ): Boolean =
        if (isOnline) {
            listOf(telegramBotResponse, telegramRecipientResponse).any {
                isNetworkUnavailableError(it)
            }
        } else {
            true
        }

    private fun isNetworkUnavailableError(response: Response<Any?, DomainError>): Boolean =
        response is Response.Failure && response.error is DomainError.NetworkUnavailable

    private fun getBotState(telegramBotResponse: Response<TelegramBot?, DomainError>): BotState =
        when (telegramBotResponse) {
            is Response.Success ->
                telegramBotResponse.data?.let { telegramBot ->
                    BotState.Configured(
                        botName = telegramBot.name, botUsername = telegramBot.username)
                } ?: BotState.NotConfigured
            is Response.Failure ->
                when (telegramBotResponse.error) {
                    is DomainError.NetworkUnavailable -> BotState.Loading
                    is DomainError.NetworkError -> BotState.Error("Network error")
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
                    is DomainError.NetworkUnavailable ->
                        RecipientState.Loading("Waiting for network...")
                    is DomainError.NetworkError ->
                        RecipientState.ExternalError("Check bot settings")
                    is DomainError.BotApiTokenMissing ->
                        RecipientState.ExternalError("Bot configuration required")
                    is DomainError.BotApiTokenInvalid ->
                        RecipientState.ExternalError("Check bot settings")
                    is DomainError.RecipientInvalid ->
                        RecipientState.RecipientError("Recipient blocked the bot")
                    else -> RecipientState.RecipientError("Unhandled error")
                }
        }

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.AddTelegramBot -> addBot(action.botApiToken)
            is SettingsAction.RemoveTelegramBot -> removeBot()
            is SettingsAction.AddRecipient -> addRecipient()
            is SettingsAction.RemoveRecipient -> removeRecipient()
        }
    }

    private fun addBot(botApiToken: String) =
        viewModelScope.launch { addTelegramBotUseCase(botApiToken) }

    private fun removeBot() = viewModelScope.launch { deleteTelegramBotUseCase() }

    private fun addRecipient() = viewModelScope.launch { addTelegramRecipientUseCase() }

    private fun removeRecipient() = viewModelScope.launch { deleteTelegramRecipientUseCase() }
}
