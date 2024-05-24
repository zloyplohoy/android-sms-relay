package ag.sokolov.smsrelay.ui.settings.recipient

sealed class RecipientSettingsAction {
    data object AddRecipient : RecipientSettingsAction()

    data object RemoveRecipient : RecipientSettingsAction()
}
