package ag.sokolov.smsrelay.ui.settings.bot

import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.use_case.add_telegram_bot.AddTelegramBotUseCase
import ag.sokolov.smsrelay.domain.use_case.delete_telegram_bot.DeleteTelegramBotUseCase
import ag.sokolov.smsrelay.domain.use_case.get_telegram_bot.GetTelegramBotUseCase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class BotSettingsViewModel
@Inject
constructor(
    private val getTelegramBotUseCase: GetTelegramBotUseCase,
    private val addTelegramBotUseCase: AddTelegramBotUseCase,
    private val deleteTelegramBotUseCase: DeleteTelegramBotUseCase
) : ViewModel() {

    var state by mutableStateOf<BotSettingsScreenState>(BotSettingsScreenState.Loading)
        private set

    init {
        observeTelegramBot()
    }

    fun onAction(action: BotSettingsAction) {
        when (action) {
            is BotSettingsAction.AddBot -> addBot(action.botApiToken)
            is BotSettingsAction.RemoveBot -> removeBot()
        }
    }

    private fun addBot(botApiToken: String) {
        viewModelScope.launch { addTelegramBotUseCase(botApiToken) }
    }

    private fun removeBot() {
        viewModelScope.launch { deleteTelegramBotUseCase() }
    }

    private fun observeTelegramBot() {
        viewModelScope.launch {
            getTelegramBotUseCase().collect { response ->
                when (response) {
                    is Response.Success ->
                        state =
                            BotSettingsScreenState.Configured(
                                botName = response.data.name, botUsername = response.data.username)
                    is Response.Failure ->
                        state =
                            if (response.error is DomainError.BotApiTokenMissing) {
                                BotSettingsScreenState.NotConfigured
                            } else {
                                BotSettingsScreenState.Error(
                                    errorMessage =
                                        when (response.error) {
                                            is DomainError.BotApiTokenInvalid ->
                                                "Invalid API token, click to update"
                                            is DomainError.NetworkUnavailable ->
                                                "Network unavailable"
                                            else -> "Unhandled error"
                                        })
                            }
                }
            }
        }
    }
}
