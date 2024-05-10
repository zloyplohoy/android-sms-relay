package ag.sokolov.smsrelay.ui.settings.recipient

data class RecipientSettingsScreenState (
    val isRecipientConfigured: Boolean = false,
    val recipientName: String = "",
    val recipientUsername: String? = null,
    val showWarning: Boolean = false,
    val showDeleteButton: Boolean = false
)
