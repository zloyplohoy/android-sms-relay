package ag.sokolov.smsrelay.ui.telegram_bot_settings

data class TelegramBotApiTokenDialogState(
    val isDialogVisible: Boolean = false,
    val tokenTextFieldValue: String = "",
    val isTokenStructureValid: Boolean = false
)
