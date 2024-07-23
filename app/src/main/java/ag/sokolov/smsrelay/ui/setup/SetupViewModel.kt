package ag.sokolov.smsrelay.ui.setup

import ag.sokolov.smsrelay.data.telegram_bot_api.TelegramBotApi
import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import ag.sokolov.smsrelay.domain.use_case.add_telegram_bot.AddTelegramBotUseCase
import ag.sokolov.smsrelay.domain.use_case.delete_telegram_bot.DeleteTelegramBotUseCase
import ag.sokolov.smsrelay.domain.use_case.get_telegram_bot_2.GetTelegramBot2UseCase
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(
    getTelegramBotUseCase: GetTelegramBot2UseCase,
    private val addTelegramBotUseCase: AddTelegramBotUseCase,
    private val deleteTelegramBotUseCase: DeleteTelegramBotUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val configurationRepository: ConfigurationRepository,
    private val telegramBotApi: TelegramBotApi
) : ViewModel() {

    var _state2 = MutableStateFlow(SetupState())
    val state2: StateFlow<SetupState> = _state2

    init {
        updateBotState()
    }


    private fun updateBotState() {
        viewModelScope.launch {
            val telegramBotApiToken = configurationRepository.getTelegramBotApiToken2()

            if (telegramBotApiToken != null) {
                val botDetails = telegramBotApi.getTelegramBot(telegramBotApiToken)
                when (botDetails) {
                    is Response.Success -> {
                        _state2.value = _state2.value.copy(
                            botState = BotState.Configured(
                                botName = botDetails.data.name,
                                botUsername = botDetails.data.username
                            )
                        )
                    }
                    is Response.Failure -> {
                        _state2.value = _state2.value.copy(
                            botState = BotState.Error(
                                errorMessage = botDetails.error.toString()
                            )
                        )
                    }
                    else -> {}
                }
                _state2.value = _state2.value.copy(botState = BotState.Loading)
            } else {
                _state2.value = _state2.value.copy(botState = BotState.NotConfigured)
            }
        }
    }

    // old implementation

    private val json = Json { encodeDefaults = true } // TODO: Why encodeDefaults?

    private fun getSavedState(): BotState =
        savedStateHandle.get<String>("state")
            ?.let { json.decodeFromString(BotState.serializer(), it) }
            ?: BotState.NotConfigured

    private fun setSavedState(state: BotState) {
        savedStateHandle["state"] = json.encodeToString(BotState.serializer(), state)
    }

    val state = getTelegramBotUseCase()
        .map { telegramBotResponse -> getBotSetupState(telegramBotResponse) }
        .preventConfiguredToLoadingTransition(::getSavedState)
        .setMinimumLoadingTime(3_000)
        .onEach { setSavedState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = getSavedState()
        )

    private val telegramApiTokenRegex = Regex("""^\d{10}:[A-Za-z0-9_-]{35}$""")

    fun onTokenValueChanged(token: String) =
        viewModelScope.launch {
            if (telegramApiTokenRegex.matches(token)) {
                addTelegramBotUseCase(token)
            }
        }

    fun onTokenReset() =
        viewModelScope.launch {
            deleteTelegramBotUseCase()
        }

    private fun getBotSetupState(telegramBotResponse: Response<TelegramBot?, DomainError>): BotState =
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
            is DomainError.NetworkUnavailable -> BotState.Error("Device is offline")
            is DomainError.NetworkError -> BotState.Error("Network error")
            is DomainError.BotApiTokenInvalid -> BotState.Error("Bot API token invalid")
            else -> BotState.Error("Unhandled error")
        }
}


fun Flow<BotState>.setMinimumLoadingTime(loadingTimeMillis: Long): Flow<BotState> =
    flow {
        var lastEmissionTime = System.currentTimeMillis()
        var lastValue: BotState = BotState.Loading

        this@setMinimumLoadingTime.collect { value ->
            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - lastEmissionTime

            if (elapsedTime >= loadingTimeMillis || lastValue !is BotState.Loading) {
                emit(value)
                lastEmissionTime = currentTime
                lastValue = value
            } else {
                delay(loadingTimeMillis - elapsedTime)
                emit(value)
                lastEmissionTime = System.currentTimeMillis()
                lastValue = value
            }
        }
    }

fun Flow<BotState>.preventConfiguredToLoadingTransition(getSavedState: () -> BotState) =
    this.filterNot { newState ->
        (getSavedState() is BotState.Configured && newState is BotState.Loading)
    }
