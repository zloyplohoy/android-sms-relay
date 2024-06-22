package ag.sokolov.smsrelay.ui.setup.screen.bot

import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.use_case.add_telegram_bot.AddTelegramBotUseCase
import ag.sokolov.smsrelay.domain.use_case.get_telegram_bot_2.GetTelegramBot2UseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BotSetupViewModel @Inject constructor(
    getTelegramBotUseCase: GetTelegramBot2UseCase,
    private val addTelegramBotUseCase: AddTelegramBotUseCase
) : ViewModel() {
    val state = getTelegramBotUseCase().map { telegramBotResponse ->
        getBotSetupState(telegramBotResponse)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BotSetupState.NotConfigured
    )

    private val telegramApiTokenRegex = Regex("""^\d{10}:[A-Za-z0-9_-]{35}$""")

    fun onTokenValueChange(token: String) =
        viewModelScope.launch {
            if (telegramApiTokenRegex.matches(token)) {
                addTelegramBotUseCase(token)
            }
        }

    private fun getBotSetupState(telegramBotResponse: Response<TelegramBot?, DomainError>): BotSetupState =
        when (telegramBotResponse) {
            is Response.Loading -> BotSetupState.Loading
            is Response.Success -> getBotStateFromData(telegramBotResponse.data)
            is Response.Failure -> getBotStateFromError(telegramBotResponse.error)
        }

    private fun getBotStateFromData(telegramBot: TelegramBot? = null): BotSetupState =
        telegramBot?.let { BotSetupState.Configured(botName = it.name, botUsername = it.username) }
            ?: BotSetupState.NotConfigured

    private fun getBotStateFromError(error: DomainError): BotSetupState =
        when (error) {
            is DomainError.NetworkUnavailable -> BotSetupState.Error("Device is offline")
            is DomainError.NetworkError -> BotSetupState.Error("Network error")
            is DomainError.BotApiTokenInvalid -> BotSetupState.Error("Bot API token invalid")
            else -> BotSetupState.Error("Unhandled error")
        }
}
