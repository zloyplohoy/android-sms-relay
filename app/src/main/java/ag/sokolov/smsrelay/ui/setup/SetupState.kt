package ag.sokolov.smsrelay.ui.setup

import ag.sokolov.smsrelay.domain.model.DomainError
import ag.sokolov.smsrelay.domain.model.Response
import ag.sokolov.smsrelay.domain.model.TelegramBot
import ag.sokolov.smsrelay.domain.model.TelegramUser

data class SetupState(
    val botState: BotState = BotState.Loading,
    val recipientState: RecipientState = RecipientState.Loading,
    val isLoading: Boolean = true
)

sealed class BotState {
    data class Configured(
        val name: String,
        val username: String
    ) : BotState()

    data class Error(
        val message: String
    ) : BotState()

    data object NotConfigured : BotState()
    data object Loading : BotState()
}

fun Response<TelegramBot, DomainError>.toBotState() =
    when (this) {
        is Response.Success -> {
            BotState.Configured(
                name = this.data.name,
                username = this.data.username
            )
        }
        is Response.Failure -> {
            BotState.Error(
                message = when (this.error) {
                    is DomainError.NetworkUnavailable -> "Device is offline"
                    is DomainError.NetworkError -> "Network error"
                    is DomainError.BotApiTokenInvalid -> "Bot API token invalid"
                    else -> "Unhandled error"
                }
            )
        }
    }

sealed class RecipientState {
    data class Configured(
        val name: String,
        val username: String? = null
    ) : RecipientState()

    data class Error(
        val message: String
    ) : RecipientState()

    data object NotConfigured : RecipientState()
    data object Loading : RecipientState()
}

fun Response<TelegramUser, DomainError>.toRecipientState() =
    when (this) {
        is Response.Success -> {
            RecipientState.Configured(
                name = this.data.lastName?.let { "${this.data.firstName} $it" }
                    ?: this.data.firstName,
                username = this.data.username
            )
        }
        is Response.Failure -> {
            RecipientState.Error(
                message = when (this.error) {
                    is DomainError.NetworkUnavailable -> "Device is offline"
                    is DomainError.NetworkError -> "Network error"
                    is DomainError.BotApiTokenInvalid -> "Bot API token invalid"
                    is DomainError.RecipientInvalid -> "Bot blocked by recipient"
                    else -> "Unhandled error"
                }
            )
        }
    }
