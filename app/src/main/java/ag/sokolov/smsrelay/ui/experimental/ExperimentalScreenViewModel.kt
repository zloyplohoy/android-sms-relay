package ag.sokolov.smsrelay.ui.experimental

import ag.sokolov.smsrelay.domain.use_case.get_telegram_recipient_id.GetTelegramRecipientIdUseCase
import ag.sokolov.smsrelay.domain.use_case.register_telegram_recipient.RegisterTelegramRecipientUseCase
import ag.sokolov.smsrelay.domain.use_case.send_telegram_message.SendTelegramMessageUseCase
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ExperimentalScreenViewModel @Inject constructor(
    private val registerTelegramRecipientUseCase: RegisterTelegramRecipientUseCase,
    private val getTelegramRecipientIdUseCase: GetTelegramRecipientIdUseCase,
    private val sendTelegramMessageUseCase: SendTelegramMessageUseCase
) : ViewModel() {
    var verificationCode = mutableStateOf("")
        private set

    var result = mutableStateOf("")
        private set

    fun onValueChange(value: String) {
        verificationCode.value = value
    }

    fun onClick() {
        viewModelScope.launch {
            registerTelegramRecipientUseCase(verificationCode.value, 10000.milliseconds).onSuccess {
                result.value = "Went OK"
            }.onFailure {
                result.value = it.message ?: "Unknown error"
            }
        }
    }

    var result2 = mutableStateOf("")
        private set

    fun onClick2() {
        viewModelScope.launch {
            getTelegramRecipientIdUseCase().onSuccess {
                result2.value = it.toString()
            }.onFailure {
                result.value = it.message ?: "Unknown error"
            }
        }
    }

    var message = mutableStateOf("")
        private set

    var result3 = mutableStateOf("")
        private set

    fun onValueChange2(value: String) {
        message.value = value
    }

    fun onClick3() {
        viewModelScope.launch {
            sendTelegramMessageUseCase(message.value).onSuccess {
                result3.value = "Success"
            }.onFailure {
                result3.value = it.message.toString()
            }
        }
    }
}
