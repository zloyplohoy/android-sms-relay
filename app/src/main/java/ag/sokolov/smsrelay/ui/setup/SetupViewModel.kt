package ag.sokolov.smsrelay.ui.setup

import ag.sokolov.smsrelay.data.telegram_bot_api.TelegramBotApi2
import ag.sokolov.smsrelay.data.telegram_config.TelegramConfig
import ag.sokolov.smsrelay.work.RecipientRegistrationWorker
import androidx.lifecycle.ViewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val telegramConfig: TelegramConfig,
    botApi2: TelegramBotApi2,
    private val workManager: WorkManager
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)

    private var isLoadingInLastRecordedState: Boolean = false

    val stateFlow: StateFlow<SetupState> = combine(
        botApi2.getTelegramBot(), botApi2.getTelegramRecipient()
    ) { getTelegramBotResponse, getTelegramRecipientResponse ->
        getTelegramBotResponse.toBotState() to getTelegramRecipientResponse.toRecipientState()
    }.map { (botState, recipientState) ->
        SetupState(
            botState = botState,
            recipientState = recipientState,
            isLoading = botState is BotState.Loading || recipientState is RecipientState.Loading
        )
    }.onEach {
        // Delay transitions from loading state to allow animations to play
        if (isLoadingInLastRecordedState) { delay(3.seconds) }
        isLoadingInLastRecordedState = it.isLoading
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(3_000),
        initialValue = SetupState()
    )

    private val telegramApiTokenRegex = Regex("""^\d{10}:[A-Za-z0-9_-]{35}$""")

    fun onTokenValueChanged(token: String) =
        scope.launch {
            if (telegramApiTokenRegex.matches(token)) {
                telegramConfig.setToken(token)
            }
        }

    fun onTokenReset() =
        scope.launch { telegramConfig.deleteToken() }

    fun onRecipientReset() =
        scope.launch { telegramConfig.deleteRecipientId() }

    fun onRecipientRegistrationStarted() {
        // TODO: Add recipient state update logic based on worker response
        val verificationCode = generateVerificationCode()
        val request: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<RecipientRegistrationWorker>()
                .setInputData(workDataOf("VERIFICATION_CODE" to verificationCode))
                .setExpedited(OutOfQuotaPolicy.DROP_WORK_REQUEST).build()
        val work = workManager.enqueueUniqueWork(
            "ADD_RECIPIENT", ExistingWorkPolicy.REPLACE, request
        )
    }

    private fun generateVerificationCode(): String =
        "%05d".format(Random.nextInt(0, 100_000))
}
