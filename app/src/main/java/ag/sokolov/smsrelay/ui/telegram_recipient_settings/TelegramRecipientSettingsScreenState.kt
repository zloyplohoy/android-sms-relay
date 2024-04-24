package ag.sokolov.smsrelay.ui.telegram_recipient_settings

data class TelegramRecipientSettingsScreenState (
    val isRecipientAdded: Boolean = false,
    val recipientName: String = "",
    val recipientUsername: String = ""
)
