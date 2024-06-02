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
import ag.sokolov.smsrelay.ui.settings.action.SettingsAction
import ag.sokolov.smsrelay.ui.settings.state.BotState
import ag.sokolov.smsrelay.ui.settings.state.RecipientState
import ag.sokolov.smsrelay.ui.settings.state.SettingsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
    private val getTelegramBotUseCase: GetTelegramBotUseCase,
    private val getTelegramRecipientUseCase: GetTelegramRecipientUseCase,
    private val addTelegramBotUseCase: AddTelegramBotUseCase,
    private val deleteTelegramBotUseCase: DeleteTelegramBotUseCase,
    private val addTelegramRecipientUseCase: AddTelegramRecipientUseCase,
    private val deleteTelegramRecipientUseCase: DeleteTelegramRecipientUseCase
) : ViewModel() {

    private val _state =
        combine(getTelegramBotUseCase(), getTelegramRecipientUseCase()) {
                telegramBotResponse,
                telegramRecipientResponse ->
                getSettingsState(telegramBotResponse, telegramRecipientResponse)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SettingsState())
    val state: StateFlow<SettingsState> = _state

    private fun getSettingsState(
        telegramBotResponse: Response<TelegramBot?, DomainError>,
        telegramRecipientResponse: Response<TelegramUser?, DomainError>
    ): SettingsState {
        return SettingsState(
            botState = getBotState(telegramBotResponse),
            recipientState = getRecipientState(telegramRecipientResponse))
    }

    private fun getBotState(telegramBotResponse: Response<TelegramBot?, DomainError>): BotState =
        when (telegramBotResponse) {
            is Response.Loading -> BotState.Loading
            is Response.Success -> getBotStateFromData(telegramBotResponse.data)
            is Response.Failure -> getBotStateFromError(telegramBotResponse.error)
        }

    private fun getBotStateFromData(telegramBot: TelegramBot? = null): BotState =
        telegramBot?.let { BotState.Configured(botName = it.name, botUsername = it.username) }
            ?: BotState.NotConfigured

    private fun getBotStateFromError(error: DomainError): BotState =
        when (error) {
            is DomainError.NetworkUnavailable -> BotState.Loading
            is DomainError.NetworkError -> BotState.Error("Network error")
            is DomainError.BotApiTokenInvalid -> BotState.Error("Bot API token invalid")
            else -> BotState.Error("Unhandled error")
        }

    private fun getRecipientState(
        telegramRecipientResponse: Response<TelegramUser?, DomainError>
    ): RecipientState =
        when (telegramRecipientResponse) {
            is Response.Loading -> RecipientState.Loading()
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
