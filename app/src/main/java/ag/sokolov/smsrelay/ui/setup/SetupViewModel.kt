package ag.sokolov.smsrelay.ui.setup

import ag.sokolov.smsrelay.data.telegram_bot_api.TelegramBotApi
import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import ag.sokolov.smsrelay.ui.setup.screen.recipient.AddRecipientWorker
import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
    private val telegramBotApi: TelegramBotApi,
    private val application: Application
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

    private fun setBotState(botState: BotState) {
        state = state.copy(botState = botState)
    }

    private suspend fun initializeBotState() {
        val token = configurationRepository.getTelegramBotApiToken()
        if (token != null) {
            setBotState(telegramBotApi.getTelegramBot(token).toBotState())
        } else {
            setBotState(BotState.NotConfigured)
        }
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

    private suspend fun addTelegramBot(token: String) {
        val minOperationTimeMillis = 3_000L
        val startTime = System.currentTimeMillis()
        val botState = telegramBotApi.getTelegramBot(token).toBotState()
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

    fun doWork() {
        val pin = (100000..999999).random().toString()
        val request: WorkRequest =
            OneTimeWorkRequestBuilder<AddRecipientWorker>()
                .setInputData(workDataOf(
                    "PIN" to pin
                ))
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()
        WorkManager.getInstance(application).enqueue(request)
    }
}
