package ag.sokolov.smsrelay.ui.setup

import ag.sokolov.smsrelay.data.telegram_bot_api.TelegramBotApi
import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
    private val telegramBotApi: TelegramBotApi
) : ViewModel() {

    var state by mutableStateOf(SetupState())
        private set

    init {
        viewModelScope.launch {
            initializeBotState()
        }
    }

    fun setLoadingState(isLoading: Boolean) {
        state = state.copy(isLoading = isLoading)
    }

    private suspend fun initializeBotState() {
        val token = configurationRepository.getTelegramBotApiToken()
        if (token != null) {
            setBotState(getBotStateFromApi(token))
        } else {
            setBotState(BotState.NotConfigured)
        }
    }

    private suspend fun addTelegramBot(token: String) {
        val minOperationTimeMillis = 3_000L
        val startTime = System.currentTimeMillis()
        val botState = getBotStateFromApi(token)
        val endTime = System.currentTimeMillis()
        if (botState is BotState.Configured) {
            configurationRepository.setTelegramBotApiToken(token)
        }
        val elapsedTime = endTime - startTime
        if (elapsedTime < minOperationTimeMillis) {
            delay(minOperationTimeMillis - elapsedTime)
        }
        setBotState(botState)
    }

    private fun setBotState(botState: BotState) {
        state = state.copy(botState = botState)
    }

    private val telegramApiTokenRegex = Regex("""^\d{10}:[A-Za-z0-9_-]{35}$""")

    fun onTokenValueChanged(token: String) =
        viewModelScope.launch {
            if (telegramApiTokenRegex.matches(token)) {
                setBotState(BotState.Loading)
                addTelegramBot(token)
            }
        }

    fun onTokenReset() =
        viewModelScope.launch {
            configurationRepository.deleteTelegramApiToken()
            setBotState(BotState.NotConfigured)
        }

    private suspend fun getBotStateFromApi(token: String): BotState =
        when (val apiResponse = telegramBotApi.getTelegramBot(token)) {
            is Response.Loading -> BotState.Loading
            is Response.Success -> {
                BotState.Configured(
                    name = apiResponse.data.name,
                    username = apiResponse.data.username
                )
            }
            is Response.Failure -> {
                BotState.Error(
                    message = when (apiResponse.error) {
                        is DomainError.NetworkUnavailable -> "Device is offline"
                        is DomainError.NetworkError -> "Network error"
                        is DomainError.BotApiTokenInvalid -> "Bot API token invalid"
                        else -> "Unhandled error"
                    }
                )
            }
        }
}
