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
import ag.sokolov.smsrelay.ui.settings.state.MenuItemState
import ag.sokolov.smsrelay.ui.settings.state.RecipientState
import ag.sokolov.smsrelay.ui.settings.state.SettingsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
    getTelegramBotUseCase: GetTelegramBotUseCase,
    getTelegramRecipientUseCase: GetTelegramRecipientUseCase,
    private val addTelegramBotUseCase: AddTelegramBotUseCase,
    private val deleteTelegramBotUseCase: DeleteTelegramBotUseCase,
    private val addTelegramRecipientUseCase: AddTelegramRecipientUseCase,
    private val deleteTelegramRecipientUseCase: DeleteTelegramRecipientUseCase
) : ViewModel() {

    val state =
        combine(getTelegramBotUseCase(), getTelegramRecipientUseCase()) {
                telegramBotResponse,
                telegramRecipientResponse ->
                getSettingsState(telegramBotResponse, telegramRecipientResponse)
            }
            .toViewModelScopeStateFlow(initialState = SettingsState())

    private fun getSettingsState(
        telegramBotResponse: Response<TelegramBot?, DomainError>,
        telegramRecipientResponse: Response<TelegramUser?, DomainError>
    ): SettingsState {
        return SettingsState(
            botState = getBotState(telegramBotResponse),
            recipientState = getRecipientState(telegramRecipientResponse),
            botMenuItemState = getBotMenuItemState(telegramBotResponse),
            recipientMenuItemState = getRecipientMenuItemState(telegramRecipientResponse))
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

    private fun getBotMenuItemState(response: Response<TelegramBot?, DomainError>) =
        when (response) {
            is Response.Loading -> MenuItemState()
            is Response.Success -> getBotMenuItemState(response.data)
            is Response.Failure -> getBotMenuItemState(response.error)
        }

    private fun getBotMenuItemState(bot: TelegramBot?) =
        MenuItemState(description = bot?.name ?: "Not  configured")

    private fun getBotMenuItemState(error: DomainError) =
        MenuItemState(
            showWarning = true,
            isEnabled = error !is DomainError.NetworkUnavailable,
            description =
                when (error) {
                    is DomainError.NetworkUnavailable -> "Waiting for network..."
                    is DomainError.BotApiTokenInvalid -> "API token invalid"
                    is DomainError.NetworkError -> "Network error"
                    else -> "Unhandled error"
                })

    private fun getRecipientMenuItemState(
        response: Response<TelegramUser?, DomainError>
    ): MenuItemState =
        when (response) {
            is Response.Loading -> MenuItemState()
            is Response.Success -> getRecipientMenuItemState(response.data)
            is Response.Failure -> getRecipientMenuItemState(response.error)
        }

    private fun getRecipientMenuItemState(recipient: TelegramUser?): MenuItemState =
        MenuItemState(description = recipient?.let { getFullName(it) } ?: "Not configured")

    private fun getFullName(telegramUser: TelegramUser): String =
        telegramUser.lastName?.let { "${telegramUser.firstName} $it" } ?: telegramUser.firstName

    private fun getRecipientMenuItemState(error: DomainError): MenuItemState =
        MenuItemState(
            showWarning =
                when (error) {
                    is DomainError.NetworkUnavailable,
                    is DomainError.BotApiTokenMissing,
                    is DomainError.BotApiTokenInvalid -> false
                    else -> true
                },
            isEnabled =
                when (error) {
                    is DomainError.NetworkUnavailable,
                    is DomainError.BotApiTokenMissing,
                    is DomainError.BotApiTokenInvalid -> false
                    else -> true
                },
            description =
                when (error) {
                    is DomainError.NetworkUnavailable,
                    is DomainError.BotApiTokenInvalid -> "Check bot status"
                    is DomainError.BotApiTokenMissing -> "Bot not configured"
                    is DomainError.RecipientInvalid -> "Bot blocked by recipient"
                    is DomainError.NetworkError -> "Network error"
                    else -> "Unhandled error"
                })

    private fun <T> Flow<T>.toViewModelScopeStateFlow(initialState: T): StateFlow<T> =
        this.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = initialState)
}
