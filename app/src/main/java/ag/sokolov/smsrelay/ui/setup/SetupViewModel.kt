package ag.sokolov.smsrelay.ui.setup

import ag.sokolov.smsrelay.data.telegram_bot_api.TelegramBotApi
import ag.sokolov.smsrelay.data.telegram_config.TelegramConfig
import ag.sokolov.smsrelay.work.RecipientRegistrationWorker
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val telegramConfig: TelegramConfig,
    private val botApi: TelegramBotApi,
    private val workManager: WorkManager
) : ViewModel() {

    var state by mutableStateOf(SetupState())
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            initializeBotState()
            initializeRecipientState()
        }
    }

    private fun setBotState(botState: BotState) {
        state = state.copy(botState = botState)
    }

    private fun setRecipientState(recipientState: RecipientState) {
        state = state.copy(recipientState = recipientState)
    }

    private suspend fun initializeBotState() {
        val token = telegramConfig.getToken()
        if (token != null) {
            setBotState(botApi.getTelegramBot(token).toBotState())
        } else {
            setBotState(BotState.NotConfigured)
        }
    }

    private suspend fun initializeRecipientState() {
        val token = telegramConfig.getToken()
        val recipientId = telegramConfig.getRecipientId()
        if (token != null && recipientId != null) {
            setRecipientState(botApi.getTelegramRecipient(token, recipientId).toRecipientState())
        } else {
            setRecipientState(RecipientState.NotConfigured)
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
            telegramConfig.deleteToken()
            setBotState(BotState.NotConfigured)
        }

    private suspend fun addTelegramBot(token: String) {
        val minOperationTimeMillis = 3_000L
        val startTime = System.currentTimeMillis()
        val botState = botApi.getTelegramBot(token).toBotState()
        val endTime = System.currentTimeMillis()
        if (botState is BotState.Configured) {
            telegramConfig.setToken(token)
        }
        val elapsedTime = endTime - startTime
        if (elapsedTime < minOperationTimeMillis) {
            delay(minOperationTimeMillis - elapsedTime)
        }
        setBotState(botState)
    }

    fun onRecipientReset() =
        viewModelScope.launch {
            telegramConfig.deleteRecipientId()
            setRecipientState(RecipientState.NotConfigured)
        }

    fun doWork() {
        // TODO: Add recipient state update logic based on worker response
        val verificationCode = generateVerificationCode()
        val request: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<RecipientRegistrationWorker>()
                .setInputData(workDataOf("VERIFICATION_CODE" to verificationCode))
                .setExpedited(OutOfQuotaPolicy.DROP_WORK_REQUEST).build()
        val work = workManager.enqueueUniqueWork(
            "ADD_RECIPIENT",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    private fun generateVerificationCode(): String =
        "%05d".format(Random.nextInt(0, 100_000))
}
