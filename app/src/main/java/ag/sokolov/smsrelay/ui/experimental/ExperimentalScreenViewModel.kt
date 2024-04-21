package ag.sokolov.smsrelay.ui.experimental

import ag.sokolov.smsrelay.domain.use_case.get_telegram_recipient_id.GetTelegramRecipientIdUseCase
import ag.sokolov.smsrelay.domain.use_case.register_telegram_recipient.RegisterTelegramRecipientUseCase
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
    private val getTelegramRecipientIdUseCase: GetTelegramRecipientIdUseCase
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
}
