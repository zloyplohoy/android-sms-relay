package ag.sokolov.smsrelay.ui.settings.common

import androidx.compose.runtime.Stable

@Stable
sealed class RecipientState {
    data object Loading : RecipientState()

    data object NotConfigured : RecipientState()

    data class Configured(val fullName: String, val username: String? = null) : RecipientState()

    data class RecipientError(val errorMessage: String? = null) : RecipientState()

    data class BotError(val errorMessage: String? = null) : RecipientState()
}
