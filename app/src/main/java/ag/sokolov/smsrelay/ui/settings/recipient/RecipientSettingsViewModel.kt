package ag.sokolov.smsrelay.ui.settings.recipient

import ag.sokolov.smsrelay.domain.use_cases.get_telegram_bot_info.GetTelegramBotInfoUseCase
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RecipientSettingsViewModel @Inject constructor(
    private val getTelegramBotInfoResultFlowUseCase: GetTelegramBotInfoUseCase
) : ViewModel() {
    var state by mutableStateOf(RecipientSettingsScreenState())
        private set

    fun onAction(action: RecipientSettingsAction) {
    }

    fun addRecipient(context: Context) {
        val verificationCode = UUID.randomUUID().toString()

        // Start workManager listener
        // Generate validation code
        // Launch intent
        val url = ""
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    }

    private suspend fun generateTgLink(verificationCode: String): String {
        val botUsername = getTelegramBotInfoResultFlowUseCase().first().getOrThrow()
        return "tg://resolve?domain=${botUsername}&start=$verificationCode"
    }
}
