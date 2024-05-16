package ag.sokolov.smsrelay.ui.settings.recipient

sealed class RecipientSettingsScreenState {
    data object Loading : RecipientSettingsScreenState()
    data object NotConfigured : RecipientSettingsScreenState()
    data class Configured(val firstName: String, val lastName: String?, val username: String?) : RecipientSettingsScreenState()
    data class GenericError(val errorMessage: String? = null) : RecipientSettingsScreenState()
    data class RecipientError(val errorMessage: String? = null): RecipientSettingsScreenState()
}
