package ag.sokolov.smsrelay.ui.setup.screen.bot

import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.use_case.add_telegram_bot.AddTelegramBotUseCase
import ag.sokolov.smsrelay.domain.use_case.delete_telegram_bot.DeleteTelegramBotUseCase
import ag.sokolov.smsrelay.domain.use_case.get_telegram_bot_2.GetTelegramBot2UseCase
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class BotSetupViewModel @Inject constructor(
    getTelegramBotUseCase: GetTelegramBot2UseCase,
    private val addTelegramBotUseCase: AddTelegramBotUseCase,
    private val deleteTelegramBotUseCase: DeleteTelegramBotUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val json = Json { encodeDefaults = true } // TODO: Why encodeDefaults?

    private fun getSavedState(): BotSetupState =
        savedStateHandle.get<String>("state")
            ?.let { json.decodeFromString(BotSetupState.serializer(), it) }
            ?: BotSetupState.NotConfigured

    private fun setSavedState(state: BotSetupState) {
        savedStateHandle["state"] = json.encodeToString(BotSetupState.serializer(), state)
    }

    val state = getTelegramBotUseCase().map { telegramBotResponse ->
        getBotSetupState(telegramBotResponse)
    }.onEach {
        Log.d("TAG", "Obtained state: ${it.javaClass.name}")
        Log.d("TAG", "Previous state: ${getSavedState().javaClass.name}")
    }.preventConfiguredToLoadingTransition(getSavedState()).setMinimumLoadingTime(3_000).onEach {
        Log.d("TAG", "Fired state: ${it.javaClass.name}")
        setSavedState(it)
    }.stateIn(
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


fun Flow<BotSetupState>.setMinimumLoadingTime(loadingTimeMillis: Long): Flow<BotSetupState> =
    flow {
        var lastEmissionTime = System.currentTimeMillis()
        var lastValue: BotSetupState = BotSetupState.Loading

        this@setMinimumLoadingTime.collect { value ->
            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - lastEmissionTime

            if (elapsedTime >= loadingTimeMillis || lastValue !is BotSetupState.Loading) {
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

fun Flow<BotSetupState>.preventConfiguredToLoadingTransition(savedState: BotSetupState) =
    this.filterNot { newState ->
        (savedState is BotSetupState.Configured && newState is BotSetupState.Loading)
    }
