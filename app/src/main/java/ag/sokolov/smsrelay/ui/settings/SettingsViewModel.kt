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
                    is DomainError.BotApiTokenInvalid ->
                        RecipientState.BotError("Check Telegram bot settings")
                    is DomainError.RecipientInvalid ->
                        RecipientState.RecipientError("Recipient blocked the bot")
                    else -> RecipientState.RecipientError("Unhandled error")
                }
        }
}
